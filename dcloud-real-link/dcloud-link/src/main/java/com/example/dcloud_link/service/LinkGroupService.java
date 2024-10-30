package com.example.dcloud_link.service;


import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.LinkGroupRequest;
import com.example.dcloud_link.controller.request.LinkGroupUpdateRequest;
import com.example.dcloud_link.entity.vo.LinkGroupVo;

import java.util.List;

/**
 * (LinkGroup)表服务接口
 *
 * @author makejava
 * @since 2024-10-29 14:02:07
 */
public interface LinkGroupService{

    /**
     * 新增分组
     */
    JsonData add(LinkGroupRequest addRequest);

    /**
     * 校验分组名称是否存在
     */
    boolean checkGroupExists(String title);

    /**
     * 删除分组
     */
    JsonData del(Long groupId);

    /**
     * 获取分组详情
     */
    LinkGroupVo detail(Long groupId);

    /**
     * 查询用户全部分组
     */
    List<LinkGroupVo> findUserAllLinkGroup();

    /**
     * 更新分组信息
     */
    int updateById(LinkGroupUpdateRequest request);
}

