package com.example.dcloud_link.controller;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.controller.request.ShortLinkAddRequest;
import com.example.dcloud_link.controller.request.ShortLinkDelRequest;
import com.example.dcloud_link.controller.request.ShortLinkPageRequest;
import com.example.dcloud_link.controller.request.ShortLinkUpdateRequest;
import com.example.dcloud_link.service.ShortLinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {
    /**
     * 服务对象
     */
    @Resource
    private ShortLinkService shortLinkService;

    /**
     * 分页查找短链
     */
    @GetMapping("page")
    public JsonData pageByGroupId(@RequestBody ShortLinkPageRequest request) {
        Map<String, Object> result = shortLinkService.page(request);
        return JsonData.buildSuccess(result);
    }

    /**
     * 新增短链
     */
    @PostMapping("add")
    public JsonData createShortLink(@RequestBody ShortLinkAddRequest shortLinkRequest) {
        return shortLinkService.createShortLink(shortLinkRequest);
    }

    /**
     * 删除短链
     */
    @PostMapping("del")
    public JsonData delShortLink(@RequestBody ShortLinkDelRequest request) {
        return shortLinkService.delShortLink(request);
    }

    /**
     * 更新短链
     */
    @PostMapping("update")
    public JsonData updateShortLink(@RequestBody ShortLinkUpdateRequest request) {
        return shortLinkService.updateShortLink(request);
    }





}

