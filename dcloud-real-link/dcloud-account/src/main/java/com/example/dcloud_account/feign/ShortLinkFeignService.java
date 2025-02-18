package com.example.dcloud_account.feign;

import com.example.dcloud_common.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dcloud-link-service")
public interface ShortLinkFeignService {


    /**
     * 检查短链是否存在
     */
    @GetMapping(value = "/api/link/v1/check",headers = {"rpc-token=${rpc.token}"})
    JsonData check(@RequestParam("shortLinkCode") String shortLinkCode);

}
