package com.example.dcloud_link.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.LinkGroupRequest;
import com.example.dcloud_link.entity.LinkGroup;
import com.example.dcloud_link.manager.LinkGroupManager;
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
    private LinkGroupManager linkGroupManager;

    @Override
    public JsonData add(LinkGroupRequest addRequest) {
        // 获取当前登录的账号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

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
    public boolean checkGroupExists(String title) {
        return linkGroupManager.checkGroupExists(title);
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
}

