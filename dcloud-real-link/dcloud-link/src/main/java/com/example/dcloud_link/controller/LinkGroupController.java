package com.example.dcloud_link.controller;

import com.example.dcloud_link.service.LinkGroupService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * (LinkGroup)表控制层
 *
 * @author makejava
 * @since 2024-10-29 14:02:02
 */
@RestController
@RequestMapping("linkGroup")
public class LinkGroupController {

    @Resource
    private LinkGroupService linkGroupService;




}

