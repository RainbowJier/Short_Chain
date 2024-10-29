package com.example.dcloud_link.manager.Impl;

import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.mapper.ShortLinkMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (ShortLink)表服务实现类
 *
 * @author makejava
 * @since 2024-10-29 14:11:16
 */

@Service
public class ShortLinkManagerImpl implements ShortLinkManager {
    @Resource
    private ShortLinkMapper shortLinkMapper;

}
