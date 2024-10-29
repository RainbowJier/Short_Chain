package com.example.dcloud_link.service.impl;

import com.example.dcloud_link.mapper.LinkGroupMapper;
import com.example.dcloud_link.service.LinkGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (LinkGroup)表服务实现类
 *
 * @author makejava
 * @since 2024-10-29 14:02:09
 */
@Service
public class LinkGroupServiceImpl implements LinkGroupService {

    @Resource
    private LinkGroupMapper linkGroupMapper;



}

