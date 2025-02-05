package com.example.dcloud_account.feign;

import com.example.dcloud_common.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dcloud-shop-service")
public interface ProductFeignService {

    /**
     * 获取流量包商品详情
     */
    @GetMapping("/api/product/v1/detail/{product_id}")
    JsonData detail(@PathVariable("product_id") long productId);

}
