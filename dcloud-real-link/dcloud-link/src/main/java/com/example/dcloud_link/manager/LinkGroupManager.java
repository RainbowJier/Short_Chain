package com.example.dcloud_link.manager;


import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.LinkGroupRequest;
import com.example.dcloud_link.entity.LinkGroup;

import java.util.List;

/**
 * (LinkGroup)表服务接口
 *
 * @author makejava
 * @since 2024-10-29 14:02:07
 */
public interface LinkGroupManager {

    /**
     * 新增分组
     */
    int add(LinkGroup linkGroup);


    /**
     * 校验分组名称是否存在
     */
    boolean checkGroupExists(String title);

    /**
     * 删除分组
     */
    int del(LinkGroup linkGroup);

    /**
     * 获取分组详情
     */
    LinkGroup detail(Long groupId, Long accountNo);

    /**
     * 获取用户所有分组
     */
    List<LinkGroup> findUserAllLinkGroup(Long accountNo);

    /**
     * 更新分组信息
     */
    int updateById(LinkGroup linkGroup);
}

