package com.example.dcloud_link.service.impl;

import com.example.dcloud_link.mapper.ShortLinkMapper;
import com.example.dcloud_link.service.ShortLinkService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * (ShortLink)表服务实现类
 *
 * @author makejava
 * @since 2024-10-29 14:11:16
 */

@Service
public class ShortLinkServiceImpl implements ShortLinkService {
    @Resource
    private ShortLinkMapper shortLinkMapper;

}
