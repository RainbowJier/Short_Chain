
package com.example.dcloud_account.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_account.controller.request.TrafficPageRequest;
import com.example.dcloud_account.controller.request.UseTrafficRequest;
import com.example.dcloud_account.entity.Product;
import com.example.dcloud_account.entity.Traffic;
import com.example.dcloud_account.entity.vo.ProductVo;
import com.example.dcloud_account.entity.vo.TrafficVo;
import com.example.dcloud_account.entity.vo.UseTrafficVo;
import com.example.dcloud_account.feign.ProductFeignService;
import com.example.dcloud_account.manager.TrafficManager;
import com.example.dcloud_account.service.TrafficService;
import com.example.dcloud_common.constant.RedisKey;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.exception.BizException;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import com.example.dcloud_common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 流量包发放
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleTrafficMessage(EventMessage eventMessage) {
        String messageType = eventMessage.getEventMessageType();   // 订单支付类型
        Long accountNo = eventMessage.getAccountNo();  // 账号

        // 订单已经支付，新增流量包
        if (messageType.equalsIgnoreCase(EventMessageType.PRODUCT_ORDER_PAY.name())) {
            // 订单信息
            String outTradeNo = eventMessage.getBizId();   // 订单号
            String content = eventMessage.getContent();
            Map<String, Object> orderInfoMap = JsonUtil.jsonStrToObj(content, Map.class);
            Integer buyNum = (Integer) orderInfoMap.get("buyNum");  // 购买数量
            String productSnapshotStr = (String) orderInfoMap.get("productSnapshot");  // 产品快照
            Product product = JsonUtil.jsonStrToObj(productSnapshotStr, Product.class);

            log.info("订单号：{}，购买数量：{}，产品快照：{}", outTradeNo, buyNum, product);

            // 流量包有效期
            LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(product.getValidDay());
            Date date = Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // 构建流量包对象
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

            // 新增流量包
            int rows = trafficManager.add(traffic);
            log.info("【流量包消费者】新增流量包:rows={},traffic={}", rows, traffic);

            // delete total traffics in the Redis.
            String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
            redisTemplate.delete(totalTrafficTimesKey);
        }

        // 免费流量包
        if (messageType.equalsIgnoreCase(EventMessageType.TRAFFIC_FREE_INIT.name())) {
            // 获取商品信息
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
    }

    /**
     * get available traffic list.
     */
    @Override
    public Map<String, Object> pageAvailable(TrafficPageRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        Page<Traffic> trafficPage = trafficManager.pageAvailable(page, size, accountNo);

        // 流量包列表
        List<Traffic> records = trafficPage.getRecords();

        // 转为vo对象
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
     * 查询某个流量包的详情
     */
    @Override
    public TrafficVo detail(long trafficId) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Traffic traffic = trafficManager.findByIdAndAccountNo(trafficId, accountNo);

        // 转为vo对象
        TrafficVo trafficVo = new TrafficVo();
        BeanUtils.copyProperties(traffic, trafficVo);
        return trafficVo;
    }


    /**
     * delete expired traffic
     * 1. get a part of traffic list randomly.
     * 2. check if the traffic is expired.
     * 3. delete expired traffic.
     * 4. if more than 25% of the traffics are expired, start again from step 1.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExpiredTraffic() {
        int count = trafficManager.deleteExpiredTraffic();
        log.info("【Schedule Tasks】 Delete expired traffics :count={}", count);

        return false;
    }

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

        // store total traffic to Redis.
        long leftSeconds = TimeUtil.getRemainSecondsOneDay(new Date());
        String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
        log.info("total traffic key : {}", totalTrafficTimesKey);


        redisTemplate.opsForValue()
                .set(totalTrafficTimesKey,
                        useTrafficVo.getDayTotalLeftTimes() - 1,
                        leftSeconds,
                        TimeUnit.SECONDS);

        return JsonData.buildSuccess();
    }

    /**
     * get non updated traffic and current used traffic.
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


}
