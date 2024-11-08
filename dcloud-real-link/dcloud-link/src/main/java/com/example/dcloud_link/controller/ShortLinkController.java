package com.example.dcloud_link.controller;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.controller.request.ShortLinkRequest;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.mapper.ShortLinkMapper;
import com.example.dcloud_link.service.ShortLinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;

/**
 * (ShortLink)表控制层
 *
 * @author makejava
 * @since 2024-10-29 14:10:42
 */
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




}

