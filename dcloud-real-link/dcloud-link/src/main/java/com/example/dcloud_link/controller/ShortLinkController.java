package com.example.dcloud_link.controller;

import com.example.dcloud_link.service.ShortLinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ShortLink)表控制层
 *
 * @author makejava
 * @since 2024-10-29 14:10:42
 */
@RestController
@RequestMapping("/api/link")
public class ShortLinkController {
    /**
     * 服务对象
     */
    @Resource
    private ShortLinkService shortLinkService;

}

