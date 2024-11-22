package com.example.dcloud_link.manager.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_common.enums.ShortLinkStateEnum;
import com.example.dcloud_link.entity.GroupCodeMapping;
import com.example.dcloud_link.entity.vo.GroupCodeMappingVo;
import com.example.dcloud_link.manager.GroupCodeMappingManager;
import com.example.dcloud_link.mapper.GroupCodeMappingMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class GroupCodeMappingManagerImpl implements GroupCodeMappingManager {

    @Resource
    private GroupCodeMappingMapper groupCodeMappingMapper;

    @Override
    public Map<String, Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId) {
        Page<GroupCodeMapping> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<GroupCodeMapping> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GroupCodeMapping::getAccountNo, accountNo)
                .eq(GroupCodeMapping::getGroupId, groupId)
                .eq(GroupCodeMapping::getDel, 0);

        // 获取查询结果
        Page<GroupCodeMapping> pageMap = groupCodeMappingMapper.selectPage(pageParam, lambdaQueryWrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("total_record", pageMap.getTotal()); // 总数据量
        result.put("total_page", pageMap.getPages());   // 总页数

        List<GroupCodeMappingVo> list = new ArrayList<>();
        List<GroupCodeMapping> records = pageMap.getRecords();  // 获取数据列表
        for (GroupCodeMapping object : records) {
            GroupCodeMappingVo groupCodeMappingVo = new GroupCodeMappingVo();
            BeanUtils.copyProperties(object, groupCodeMappingVo);
            list.add(groupCodeMappingVo);
        }
        result.put("current_data", list); // 当前页的数据量

        return result;
    }


    @Override
    public GroupCodeMapping findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId) {
        LambdaQueryWrapper<GroupCodeMapping> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(GroupCodeMapping::getId, mappingId)
                .eq(GroupCodeMapping::getAccountNo, accountNo)
                .eq(GroupCodeMapping::getGroupId, groupId);

        return groupCodeMappingMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public int add(GroupCodeMapping groupCodeMapping) {
        return groupCodeMappingMapper.insert(groupCodeMapping);
    }

    @Override
    public int del(GroupCodeMapping groupCodeMapping) {
        LambdaUpdateWrapper<GroupCodeMapping> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(GroupCodeMapping::getId, groupCodeMapping.getId())
                // mapping 是通过groupId和accountNo进行分库分表的，所以要加上，不加上会遍历所有数据库节点
                .eq(GroupCodeMapping::getAccountNo, groupCodeMapping.getAccountNo())
                .eq(GroupCodeMapping::getGroupId, groupCodeMapping.getGroupId())
                .set(GroupCodeMapping::getDel, groupCodeMapping.getDel());

        return groupCodeMappingMapper.update(wrapper);
    }

    @Override
    public int update(GroupCodeMapping groupCodeMapping) {
        LambdaUpdateWrapper<GroupCodeMapping> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(GroupCodeMapping::getId, groupCodeMapping.getId())
                .eq(GroupCodeMapping::getAccountNo, groupCodeMapping.getAccountNo())
                .eq(GroupCodeMapping::getGroupId, groupCodeMapping.getGroupId())
                .eq(GroupCodeMapping::getDel, groupCodeMapping.getDel())
                .set(GroupCodeMapping::getTitle, groupCodeMapping.getTitle());
        return groupCodeMappingMapper.update(wrapper);
    }


    @Override
    public int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum) {
        LambdaUpdateWrapper<GroupCodeMapping> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(GroupCodeMapping::getAccountNo, accountNo)
                .eq(GroupCodeMapping::getGroupId, groupId)
                .eq(GroupCodeMapping::getCode, shortLinkCode)
                .set(GroupCodeMapping::getState, shortLinkStateEnum.name())
                .eq(GroupCodeMapping::getDel, 0);

        return groupCodeMappingMapper.update(lambdaUpdateWrapper);
    }

    /**
     * 判断短链码是否存在
     */
    @Override
    public GroupCodeMapping findByCodeAndGroupId(String shortLinkCode, Long groupId, Long accountNo) {
        LambdaQueryWrapper<GroupCodeMapping> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GroupCodeMapping::getCode, shortLinkCode)
                .eq(GroupCodeMapping::getAccountNo, accountNo)
                .eq(GroupCodeMapping::getGroupId, groupId)
                .eq(GroupCodeMapping::getDel, 0);

        return groupCodeMappingMapper.selectOne(lambdaQueryWrapper);
    }

}

