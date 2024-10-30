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
import java.util.List;

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
        queryWrapper
                .eq(LinkGroup::getAccountNo, linkGroup.getAccountNo())
                .eq(LinkGroup::getId, linkGroup.getId());

        return linkGroupMapper.delete(queryWrapper);
    }

    @Override
    public LinkGroup detail(Long groupId, Long accountNo) {
        LambdaQueryWrapper<LinkGroup> queryWrapper = new LambdaQueryWrapper<>();
        // 账号和id都要匹配才行
        queryWrapper
                .eq(LinkGroup::getAccountNo, accountNo)
                .eq(LinkGroup::getId, groupId);

        return linkGroupMapper.selectOne(queryWrapper);
    }

    @Override
    public List<LinkGroup> findUserAllLinkGroup(Long accountNo) {
        LambdaQueryWrapper<LinkGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LinkGroup::getAccountNo, accountNo);

        return linkGroupMapper.selectList(queryWrapper);
    }

    @Override
    public int updateById(LinkGroup linkGroup) {
        LambdaQueryWrapper<LinkGroup> queryWrapper = new LambdaQueryWrapper<>();
        
        // 账号和id都要匹配才行
        queryWrapper
                .eq(LinkGroup::getAccountNo, linkGroup.getAccountNo())
                .eq(LinkGroup::getId, linkGroup.getId());

        return linkGroupMapper.update(linkGroup, queryWrapper);
    }
}

