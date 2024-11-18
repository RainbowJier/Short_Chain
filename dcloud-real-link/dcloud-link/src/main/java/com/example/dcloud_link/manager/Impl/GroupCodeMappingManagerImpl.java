package com.example.dcloud_link.manager.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_common.enums.ShortLinkStateEnum;
import com.example.dcloud_link.entity.GroupCodeMapping;
import com.example.dcloud_link.entity.vo.GroupCodeMappingVo;
import com.example.dcloud_link.manager.GroupCodeMappingManager;
import com.example.dcloud_link.mapper.GroupCodeMappingMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
public class GroupCodeMappingManagerImpl implements GroupCodeMappingManager {

    @Resource
    private GroupCodeMappingMapper groupCodeMappingMapper;

    @Override
    public GroupCodeMapping findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId) {
        LambdaQueryWrapper<GroupCodeMapping> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GroupCodeMapping::getId, mappingId)
                .eq(GroupCodeMapping::getAccountNo, accountNo)
                .eq(GroupCodeMapping::getGroupId, groupId);

        return groupCodeMappingMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public int add(GroupCodeMapping groupCodeMapping) {
        return groupCodeMappingMapper.insert(groupCodeMapping);
    }

    @Override
    public int del(String shortLinkCode, Long accountNo, Long groupId) {
        LambdaUpdateWrapper<GroupCodeMapping> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(GroupCodeMapping::getCode, shortLinkCode)
                .eq(GroupCodeMapping::getAccountNo, accountNo)
                .eq(GroupCodeMapping::getGroupId, groupId)
                .set(GroupCodeMapping::getState, 1);

        return groupCodeMappingMapper.update(lambdaUpdateWrapper);
    }

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
        result.put("total_record", pageMap.getTotal()); // 总记录数
        result.put("total_page", pageMap.getPages());  // 总页数

        List<GroupCodeMappingVo> list = new ArrayList<>();
        List<GroupCodeMapping> records = pageMap.getRecords();
        for (GroupCodeMapping object : records) {
            GroupCodeMappingVo groupCodeMappingVo = new GroupCodeMappingVo();
            BeanUtils.copyProperties(object, groupCodeMappingVo);
            list.add(groupCodeMappingVo);
        }
        result.put("current_data", list); // 当前页的数据量

        return result;
    }


    @Override
    public int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum) {
        LambdaUpdateWrapper<GroupCodeMapping> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(GroupCodeMapping::getAccountNo, accountNo)
                .eq(GroupCodeMapping::getGroupId, groupId)
                .eq(GroupCodeMapping::getCode, shortLinkCode)
                .set(GroupCodeMapping::getState, shortLinkStateEnum.name());

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
                .eq(GroupCodeMapping::getGroupId, groupId);

        return groupCodeMappingMapper.selectOne(lambdaQueryWrapper);
    }

}

