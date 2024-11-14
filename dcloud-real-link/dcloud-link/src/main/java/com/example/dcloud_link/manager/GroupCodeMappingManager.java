package com.example.dcloud_link.manager;


import com.example.dcloud_common.enums.ShortLinkStateEnum;
import com.example.dcloud_link.entity.GroupCodeMapping;

import java.util.Map;


/**
 * B 端，商家短链码
 */
public interface GroupCodeMappingManager {

    /**
     * 查找详情
     * @param mappingId ：详情id
     */
    GroupCodeMapping findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId);

    /**
     * 新增商家短链码
     */
    int add(GroupCodeMapping groupCodeMapping);

    /**
     * 删除商家短链码
     */
    int del(String shortLinkCode, Long accountNo, Long groupId);

    /**
     * 分页查询
     */
    Map<String,Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId);

    /**
     * 更新短链的状态
     */
    int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum);
}

