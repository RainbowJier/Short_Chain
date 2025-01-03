package com.example.dcloud_link.controller;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.ShortLinkAddRequest;
import com.example.dcloud_link.controller.request.ShortLinkDelRequest;
import com.example.dcloud_link.controller.request.ShortLinkPageRequest;
import com.example.dcloud_link.controller.request.ShortLinkUpdateRequest;
import com.example.dcloud_link.entity.vo.ShortLinkVo;
import com.example.dcloud_link.service.ShortLinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {
    @Resource
    private ShortLinkService shortLinkService;

    @Value("${rpc.token}")
    private String rpcToken;

    @GetMapping("check")
    public JsonData check(@RequestParam("shortLinkCode") String shortLinkCode, HttpServletRequest request) {
        String token = request.getHeader("rpc-token");

        if (rpcToken.equalsIgnoreCase(token)) {
            ShortLinkVo shortLinkVo = shortLinkService.parseShortLinkCode(shortLinkCode);
            return shortLinkVo == null ? JsonData.buildError("Short link is not found.") : JsonData.buildSuccess();
        } else {
            return JsonData.buildError("Illegal Access.");
        }
    }

    /**
     * B-end, get short link by page.
     */
    @GetMapping("page")
    public JsonData pageByGroupId(@RequestBody ShortLinkPageRequest request) {
        Map<String, Object> result = shortLinkService.page(request);
        return JsonData.buildSuccess(result);
    }


    /**
     * add short link.
     */
    @PostMapping("add")
    public JsonData createShortLink(@RequestBody ShortLinkAddRequest shortLinkRequest) {
        return shortLinkService.createShortLink(shortLinkRequest);
    }

    /**
     * delete short link.
     */
    @PostMapping("del")
    public JsonData delShortLink(@RequestBody ShortLinkDelRequest request) {
        return shortLinkService.delShortLink(request);
    }

    /**
     * update short link.
     */
    @PostMapping("update")
    public JsonData updateShortLink(@RequestBody ShortLinkUpdateRequest request) {
        return shortLinkService.updateShortLink(request);
    }


}

