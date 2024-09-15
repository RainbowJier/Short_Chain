package com.example.dcloudaccount.controller;

import com.example.dcloudaccount.service.NotifyService;
import com.example.dcloudcommon.util.CommonUtil;
import com.example.dcloudcommon.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/8/29 21:28
 */
@RestController
@RequestMapping("/api/account/v1")
@Slf4j
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @RequestMapping("/notify")
    public JsonData notify(String message) {
        notifyService.testNotify();
        return JsonData.buildSuccess();
    }
}
