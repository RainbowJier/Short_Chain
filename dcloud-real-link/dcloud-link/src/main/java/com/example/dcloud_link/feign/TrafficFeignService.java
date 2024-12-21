package com.example.dcloud_link.feign;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.UseTrafficRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dcloud-account-service")
public interface TrafficFeignService {

    /**
     * use traffic.
     */
    @PostMapping(value = "/api/traffic/v1/reduce",headers = {"rpc-token=${rpc.token}"})
    JsonData useTraffic(@RequestBody UseTrafficRequest request);

}
