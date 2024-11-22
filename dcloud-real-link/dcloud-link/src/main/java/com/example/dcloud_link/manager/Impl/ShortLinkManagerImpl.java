package com.example.dcloud_link.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.mapper.ShortLinkMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (ShortLink)表服务实现类
 *
 * @author makejava
 * @since 2024-10-29 14:11:16
 */

@Component
public class ShortLinkManagerImpl implements ShortLinkManager {
    @Resource
    private ShortLinkMapper shortLinkMapper;

    @Override
    public int addShortLink(ShortLink shortLink) {
        return shortLinkMapper.insert(shortLink);
    }

    @Override
    public ShortLink findbyShortLink(String shortLinkCode) {
        LambdaQueryWrapper<ShortLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShortLink::getCode, shortLinkCode)
                .eq(ShortLink::getDel, 0);
        return shortLinkMapper.selectOne(wrapper);
    }

    @Override
    public int del(ShortLink shortLink) {
        LambdaUpdateWrapper<ShortLink> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ShortLink::getCode, shortLink.getCode())
                .set(ShortLink::getDel, shortLink.getDel());
        return shortLinkMapper.update(wrapper);
    }

    @Override
    public int update(ShortLink shortLink) {
        LambdaUpdateWrapper<ShortLink> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ShortLink::getCode, shortLink.getCode())
                .eq(ShortLink::getDel, shortLink.getDel())
                .set(ShortLink::getTitle, shortLink.getTitle());
        return shortLinkMapper.update(wrapper);
    }
}
