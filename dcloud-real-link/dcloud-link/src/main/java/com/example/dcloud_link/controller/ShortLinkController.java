package com.example.dcloud_link.controller;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.controller.request.ShortLinkAddRequest;
import com.example.dcloud_link.service.ShortLinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {
    /**
     * 服务对象
     */
    @Resource
    private ShortLinkService shortLinkService;

    @Resource
    private ShortLinkComponent shortLinkComponent;

    @PostMapping("add")
    public JsonData createShortLink(@RequestBody ShortLinkAddRequest shortLinkRequest) {
        return shortLinkService.createShortLink(shortLinkRequest);
    }
}

