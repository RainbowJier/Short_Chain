
package com.example.dcloud_account.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_account.controller.request.TrafficPageRequest;
import com.example.dcloud_account.entity.Product;
import com.example.dcloud_account.entity.Traffic;
import com.example.dcloud_account.entity.vo.ProductVo;
import com.example.dcloud_account.entity.vo.TrafficVo;
import com.example.dcloud_account.feign.ProductFeignService;
import com.example.dcloud_account.manager.TrafficManager;
import com.example.dcloud_account.service.TrafficService;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

    @Resource
    private TrafficManager trafficManager;

    @Resource
    private ProductFeignService productFeignService;

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
        }

        // 免费流量包
        if(messageType.equalsIgnoreCase(EventMessageType.TRAFFIC_FREE_INIT.name())){
            // 获取商品信息
            long productId = Long.parseLong(eventMessage.getBizId());
            JsonData jsonData = productFeignService.detail(productId);

            ProductVo productVo = jsonData.getData(new TypeReference<ProductVo>(){});
            //ProductVo productVo = (ProductVo) jsonData.getData();

            // 构建流量包对象
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
     * 查询可用流量包列表
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

        // 封装查询结果
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", trafficPage.getTotal()); // 总记录数
        pageMap.put("total_page", trafficPage.getPages());   // 总页数
        pageMap.put("current_data", trafficVoList);      // 当前页数据

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
}
