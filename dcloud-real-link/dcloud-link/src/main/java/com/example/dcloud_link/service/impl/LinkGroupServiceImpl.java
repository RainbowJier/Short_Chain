package com.example.dcloud_link.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.LinkGroupRequest;
import com.example.dcloud_link.controller.request.LinkGroupUpdateRequest;
import com.example.dcloud_link.entity.LinkGroup;
import com.example.dcloud_link.entity.vo.LinkGroupVo;
import com.example.dcloud_link.manager.LinkGroupManager;
import com.example.dcloud_link.mapper.LinkGroupMapper;
import com.example.dcloud_link.service.LinkGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * (LinkGroup)表服务实现类
 *
 * @author makejava
 * @since 2024-10-29 14:02:09
 */
@Service
public class LinkGroupServiceImpl implements LinkGroupService {

    @Resource
    private LinkGroupManager linkGroupManager;

    @Override
    public JsonData add(LinkGroupRequest addRequest) {
        // 获取当前登录的账号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 校验分组是否已经存在
        if (linkGroupManager.checkGroupExists(addRequest.getTitle(),accountNo)) {
            return JsonData.buildError("分组名称已存在");
        }

        // 封装参数
        LinkGroup linkGroup = new LinkGroup()
                .setTitle(addRequest.getTitle())
                .setAccountNo(accountNo);

        // 新增
        int insertRow = linkGroupManager.add(linkGroup);
        if(insertRow <= 0){
            return JsonData.buildResult(BizCodeEnum.GROUP_ADD_FAILED);
        }

        return JsonData.buildSuccess("新增成功");
    }

    @Override
    public JsonData del(Long groupId) {
        // 获取当前登录的账号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 封装参数
        LinkGroup linkGroup = new LinkGroup()
                .setAccountNo(accountNo)
                .setId(groupId);

        int deleteRow = linkGroupManager.del(linkGroup);
        if(deleteRow <= 0){
            return JsonData.buildResult(BizCodeEnum.GROUP_NOT_EXIST);
        }

        return JsonData.buildSuccess("删除成功");
    }

    @Override
    public LinkGroupVo detail(Long groupId) {
        // 获取当前登录的账号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 获取分组详情
        LinkGroup linkGroup = linkGroupManager.detail(groupId, accountNo);

        // 数据脱敏
        LinkGroupVo linkGroupVo = new LinkGroupVo();
        BeanUtils.copyProperties(linkGroup, linkGroupVo);

        return linkGroupVo;
    }

    @Override
    public List<LinkGroupVo> findUserAllLinkGroup() {
        // 获取当前登录的账号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 获取用户所有分组
        List<LinkGroup> list  = linkGroupManager.findUserAllLinkGroup(accountNo);

        // 数据脱敏
        List<LinkGroupVo> linkGroupVoList = new ArrayList<>();
        for(LinkGroup obj : list) {
            LinkGroupVo linkGroupVo = new LinkGroupVo();
            BeanUtils.copyProperties(obj, linkGroupVo);
            linkGroupVoList.add(linkGroupVo);
        }

        return linkGroupVoList;
    }

    @Override
    public JsonData updateById(LinkGroupUpdateRequest request) {
        // 获取当前登录的账号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 校验分组是否已经存在
        if (linkGroupManager.checkGroupExists(request.getTitle(),accountNo)) {
            return JsonData.buildError("分组名称已存在");
        }

        LinkGroup linkGroup = new LinkGroup()
                .setAccountNo(accountNo)
                .setId(request.getId())
                .setTitle(request.getTitle());

        int updateRow = linkGroupManager.updateById(linkGroup);
        if(updateRow <= 0){
            return JsonData.buildError("更新失败");
        }
        return JsonData.buildSuccess("更新成功");
    }
}

