package com.example.dcloud_account.controller;

import com.example.dcloud_account.controller.request.TrafficPageRequest;
import com.example.dcloud_account.controller.request.UseTrafficRequest;
import com.example.dcloud_account.entity.vo.TrafficVo;
import com.example.dcloud_account.service.TrafficService;
import com.example.dcloud_common.util.JsonData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/12/12 11:09
 * @Version: 1.0
 */


@RestController
@RequestMapping("/api/traffic/v1")
public class TrafficController {
    @Resource
    private TrafficService trafficService;

    @Value("${rpc.token}")
    private String rpcToken;

    /**
     * 分页查询可用流量包
     */
    @PostMapping("/page")
    public JsonData pageAvailable(@RequestBody TrafficPageRequest request) {
        Map<String, Object> map =  trafficService.pageAvailable(request);
        return JsonData.buildSuccess(map);
    }

    /**
     * 查找某个流量包的使用情况
     */
    @GetMapping("/detail/{trafficId}")
    public JsonData detail(@PathVariable("trafficId") long trafficId){
        TrafficVo trafficVO = trafficService.detail(trafficId);
        return JsonData.buildSuccess(trafficVO);
    }

    /**
     * reduce traffic
     */
    @PostMapping("reduce")
    public JsonData useTraffic(@RequestBody UseTrafficRequest useTrafficRequest, HttpServletRequest request){
        String requestToken = request.getHeader("rcp-token");
        if(requestToken.equals(rpcToken)){
            return trafficService.reduce(useTrafficRequest);
        }else{
            return JsonData.buildError("Illegal Access!!!");
        }
    }

}
