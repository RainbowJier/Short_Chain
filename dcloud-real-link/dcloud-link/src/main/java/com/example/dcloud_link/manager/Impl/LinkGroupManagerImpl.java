package com.example.dcloud_link.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.LinkGroupRequest;
import com.example.dcloud_link.entity.LinkGroup;
import com.example.dcloud_link.manager.LinkGroupManager;
import com.example.dcloud_link.mapper.LinkGroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (LinkGroup)表服务实现类
 *
 * @author makejava
 * @since 2024-10-29 14:02:09
 */
@Component
@Slf4j
public class LinkGroupManagerImpl implements LinkGroupManager {

    @Resource
    private LinkGroupMapper linkGroupMapper;

    @Override
    public int add(LinkGroup linkGroup) {
        return linkGroupMapper.insert(linkGroup);
    }

    @Override
    public boolean checkGroupExists(String title) {
        LambdaQueryWrapper<LinkGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LinkGroup::getTitle, title);
        LinkGroup linkGroup = linkGroupMapper.selectOne(queryWrapper);
        return linkGroup != null;
    }

    @Override
    public int del(LinkGroup linkGroup) {
        LambdaQueryWrapper<LinkGroup> queryWrapper = new LambdaQueryWrapper<>();
        // 账号和id都要匹配才行
        queryWrapper.eq(LinkGroup::getAccountNo, linkGroup.getAccountNo())
                    .eq(LinkGroup::getId, linkGroup.getId());

        return linkGroupMapper.delete(queryWrapper);
    }
}

