
package com.example.dcloud_account.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_account.config.RabbitMQConfig;
import com.example.dcloud_account.controller.request.TrafficPageRequest;
import com.example.dcloud_account.controller.request.UseTrafficRequest;
import com.example.dcloud_account.feign.ProductFeignService;
import com.example.dcloud_account.feign.ShortLinkFeignService;
import com.example.dcloud_account.manager.TrafficManager;
import com.example.dcloud_account.manager.TrafficTaskManager;
import com.example.dcloud_account.model.entity.Product;
import com.example.dcloud_account.model.entity.Traffic;
import com.example.dcloud_account.model.entity.TrafficTask;
import com.example.dcloud_account.model.vo.ProductVo;
import com.example.dcloud_account.model.vo.TrafficVo;
import com.example.dcloud_account.model.vo.UseTrafficVo;
import com.example.dcloud_account.service.TrafficService;
import com.example.dcloud_common.constant.RedisKey;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.enums.TaskStateEnum;
import com.example.dcloud_common.exception.BizException;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import com.example.dcloud_common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

    @Resource
    private TrafficManager trafficManager;

    @Resource
    private ProductFeignService productFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private TrafficTaskManager trafficTaskManager;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMQConfig rabbitMQConfig;

    @Resource
    private ShortLinkFeignService shortLinkFeignService;

    /**
     * reduce traffic.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData reduce(UseTrafficRequest useTrafficRequest) {
        Long accountNo = useTrafficRequest.getAccountNo();

        // get not updated traffic and current used traffic.
        UseTrafficVo useTrafficVo = getUseTrafficVo(accountNo);
        log.info("today's total left times:{}, current traffic:{}", useTrafficVo.getDayTotalLeftTimes(), useTrafficVo.getCurrentTraffic());
        if (useTrafficVo.getCurrentTraffic() == null) {
            return JsonData.buildResult(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }

        // update traffics that are not updated today.
        log.info("not updated traffic id list: {}", useTrafficVo.getUnUpdatedTrafficIds());
        if (!useTrafficVo.getUnUpdatedTrafficIds().isEmpty()) {
            int updateRow = trafficManager.batchUpdateUsedTimes(accountNo, useTrafficVo.getUnUpdatedTrafficIds());
            log.info("update not updated traffic:rows={}", updateRow);
        }

        // reduce traffic.
        int rows = trafficManager.addDayUsedTimes(accountNo, useTrafficVo.getCurrentTraffic().getId(), 1);
        if (rows != 1) {
            throw new BizException(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }

        // add traffic task.
        TrafficTask trafficTask = new TrafficTask();
        trafficTask.setAccountNo(accountNo)
                .setBizId(useTrafficRequest.getBizId())
                .setUseTimes(1)
                .setTrafficId(useTrafficVo.getCurrentTraffic().getId())
                .setLockState(TaskStateEnum.LOCK.name());

        trafficTaskManager.add(trafficTask);

        EventMessage trafficUseEventMessage = new EventMessage();
        trafficUseEventMessage
                .setAccountNo(accountNo)
                .setBizId(String.valueOf(trafficTask.getId()))
                .setEventMessageType(EventMessageType.TRAFFIC_USED.name());

        rabbitTemplate.convertAndSend(
                rabbitMQConfig.getTrafficEventExchange(),
                rabbitMQConfig.getTrafficReleaseDelayRoutingKey(),
                trafficUseEventMessage);

        // store total traffic to Redis.
        long leftSeconds = TimeUtil.getRemainSecondsOneDay(new Date());
        String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);

        stringRedisTemplate.opsForValue().set(
                totalTrafficTimesKey,
                String.valueOf(useTrafficVo.getDayTotalLeftTimes() - 1),
                leftSeconds,
                TimeUnit.SECONDS);

        return JsonData.buildSuccess();
    }

    /**
     * delete expired traffic
     * 1. get a part of traffic list randomly.
     * 2. check if the traffic is expired.
     * 3. delete expired traffic.
     * 4. if more than 30% of the traffics are expired, start again from step 1.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExpiredTraffic() {
        List<Long> trafficList = new ArrayList<>();
        int expiredTrafficCount = 0;

        int randomCount = 50;
        List<Traffic> list = trafficManager.selectRandomTraffics(randomCount);

        // check if the traffic is expired.
        for (Traffic traffic : list) {
            Date expiredDate = traffic.getExpiredDate();
            Long id = traffic.getId();
            if (expiredDate.before(new Date())) {
                trafficList.add(id);
                expiredTrafficCount++;
            }
        }

        int count = trafficManager.deleteExpiredTraffic(trafficList);
        log.info("===Schedule Tasks=== \n Delete expired traffics :count={}", count);

        if (expiredTrafficCount > randomCount * 0.3) {
            deleteExpiredTraffic();
        }

        return false;
    }

    /**
     * Get not-updated traffic and current used traffic.
     */
    private UseTrafficVo getUseTrafficVo(Long accountNo) {
        // get valid traffic list by accountNo.
        List<Traffic> list = trafficManager.selectAvailableTraffics(accountNo);

        String currentTime = TimeUtil.format(new Date(), "yyyy-MM-dd");
        int dayTotalLeftTimes = 0;
        Traffic currentTraffic = null;
        List<Long> unUpdatedTrafficIds = new ArrayList<>();

        for (Traffic traffic : list) {
            String trafficUpdateTime = TimeUtil.format(traffic.getGmtModified(), "yyyy-MM-dd");

            // updated traffic.
            if (currentTime.equals(trafficUpdateTime)) {
                int dayLeftTimes = traffic.getDayLimit() - traffic.getDayUsed();

                dayTotalLeftTimes += dayLeftTimes;

                // get current traffic.
                if (currentTraffic == null && dayLeftTimes > 0) {
                    currentTraffic = traffic;
                }
            }
            // not updated traffic.
            else {
                dayTotalLeftTimes += traffic.getDayLimit();

                // record not updated traffic id.
                unUpdatedTrafficIds.add(traffic.getId());

                if (currentTraffic == null) {
                    currentTraffic = traffic;
                }
            }
        }

        return new UseTrafficVo(dayTotalLeftTimes, currentTraffic, unUpdatedTrafficIds);
    }

    /**
     * Query available traffic list.
     */
    @Override
    public Map<String, Object> pageAvailable(TrafficPageRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        Page<Traffic> trafficPage = trafficManager.pageAvailable(page, size, accountNo);

        List<Traffic> records = trafficPage.getRecords();
        List<TrafficVo> trafficVoList = new ArrayList<>();
        for (Traffic traffic : records) {
            TrafficVo trafficVo = new TrafficVo();
            BeanUtils.copyProperties(traffic, trafficVo);
            trafficVoList.add(trafficVo);
        }

        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", trafficPage.getTotal()); // total records.
        pageMap.put("total_page", trafficPage.getPages());   // total pages.
        pageMap.put("current_data", trafficVoList);      // the current page data.

        return pageMap;
    }

    /**
     * get traffic detail.
     */
    @Override
    public TrafficVo detail(long trafficId) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Traffic traffic = trafficManager.findByIdAndAccountNo(trafficId, accountNo);

        TrafficVo trafficVo = new TrafficVo();
        BeanUtils.copyProperties(traffic, trafficVo);
        return trafficVo;
    }

    // ==========================Rabbit MQ Handler============================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleTrafficMessage(EventMessage eventMessage) {
        String messageType = eventMessage.getEventMessageType();

        // Paid traffic package.
        if (messageType.equalsIgnoreCase(EventMessageType.PRODUCT_ORDER_PAY.name())) {
            paidTraffic(eventMessage);
        }
        // Free traffic init.
        else if (messageType.equalsIgnoreCase(EventMessageType.TRAFFIC_FREE_INIT.name())) {
            freeTraffic(eventMessage);
        }
        // save traffic used task.
        else if (messageType.equalsIgnoreCase(EventMessageType.TRAFFIC_USED.name())) {
            trafficTask(eventMessage);
        }
    }

    private void paidTraffic(EventMessage eventMessage) {
        // 订单信息
        Long accountNo = eventMessage.getAccountNo();
        String outTradeNo = eventMessage.getBizId();   // 订单号
        String content = eventMessage.getContent();
        Map<String, Object> orderInfoMap = JsonUtil.jsonStrToObj(content, Map.class);
        Integer buyNum = (Integer) orderInfoMap.get("buyNum");  // 购买数量
        String productSnapshotStr = (String) orderInfoMap.get("productSnapshot");  // 产品快照
        Product product = JsonUtil.jsonStrToObj(productSnapshotStr, Product.class);

        log.info("===Purchase traffics=== \n " +
                "Order Number: {} \n " +
                "Number of purchases: {} \n " +
                "Product Snapshot: {}", outTradeNo, buyNum, product);

        LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(product.getValidDay());
        Date date = Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Traffic traffic = Traffic.builder()
                .accountNo(accountNo)
                .dayLimit(product.getDayTimes() * buyNum)
                .dayUsed(0)
                .totalLimit(product.getTotalTimes())
                .pluginType(product.getPluginType())
                .level(product.getLevel())
                .productId(product.getId())
                .outTradeNo(outTradeNo)
                .expiredDate(date)
                .build();

        int rows = trafficManager.add(traffic);
        log.info("===Purchase traffics=== \n " +
                "rows = {},traffic={}", rows, traffic);

        // delete total traffics in the Redis.
        String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
        stringRedisTemplate.delete(totalTrafficTimesKey);
    }

    private void freeTraffic(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();

        // get free traffic product detail.
        long productId = Long.parseLong(eventMessage.getBizId());
        JsonData jsonData = productFeignService.detail(productId);

        ProductVo productVo = jsonData.getData(new TypeReference<ProductVo>() {
        });

        Traffic traffic = Traffic.builder()
                .accountNo(accountNo)
                .dayLimit(productVo.getDayTimes())
                .dayUsed(0)
                .totalLimit(productVo.getTotalTimes())
                .pluginType(productVo.getPluginType())
                .level(productVo.getLevel())
                .productId(productVo.getId())
                .outTradeNo("free_init")
                .expiredDate(new Date())
                .build();

        trafficManager.add(traffic);
    }

    private void trafficTask(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();
        Long trafficTaskId = Long.valueOf(eventMessage.getBizId());
        TrafficTask trafficTask = trafficTaskManager.findByIdAndAccountNo(trafficTaskId, accountNo);

        if (trafficTask != null && trafficTask.getLockState().equals(TaskStateEnum.LOCK.name())) {
            // RPC to check if the short link exists.
            JsonData jsonData = shortLinkFeignService.check(trafficTask.getBizId());

            if (jsonData.getCode() != 0) {
                log.error("Failed to generate short link!!!");

                // Restore the number of times the traffic has been used.
                String useDateStr = TimeUtil.format(trafficTask.getGmtCreate(), "yyyy-MM-dd");
                trafficManager.restoreUsedTimes(accountNo, trafficTask.getTrafficId(), 1, useDateStr);

                // Remove the key that records the total available number of traffics.
                String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
                stringRedisTemplate.delete(totalTrafficTimesKey);
            }

            // delete traffic task.
            // You can use different ways to delete the traffic task.
            trafficTaskManager.deleteByIdAndAccountNo(trafficTaskId, accountNo);
        }
    }

}
