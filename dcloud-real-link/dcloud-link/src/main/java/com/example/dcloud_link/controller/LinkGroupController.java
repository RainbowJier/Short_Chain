package com.example.dcloud_link.controller;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.LinkGroupRequest;
import com.example.dcloud_link.controller.request.LinkGroupUpdateRequest;
import com.example.dcloud_link.entity.LinkGroup;
import com.example.dcloud_link.entity.vo.LinkGroupVo;
import com.example.dcloud_link.service.LinkGroupService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (LinkGroup)表控制层
 *
 * @author makejava
 * @since 2024-10-29 14:02:02
 */
@RestController
@RequestMapping("/api/group/v1")
public class LinkGroupController {

    @Resource
    private LinkGroupService linkGroupService;

    /**
     * 创建分组
     */
    @PostMapping("/add")
    public JsonData add(@RequestBody LinkGroupRequest addRequest) {
        // 校验分组是否已经存在
        if (linkGroupService.checkGroupExists(addRequest.getTitle())) {
            return JsonData.buildError("分组名称已存在");
        }

        return linkGroupService.add(addRequest);
    }

    /**
     * 删除分组
     */
    @DeleteMapping("/del/{group_id}")
    public JsonData del(@PathVariable("group_id") Long groupId) {
        return linkGroupService.del(groupId);
    }

    /**
     * 获取分组详情
     */
    @GetMapping("/detail/{group_id}")
    public JsonData detail(@PathVariable("group_id") Long groupId) {
        LinkGroupVo linkGroupVo = linkGroupService.detail(groupId);
        return JsonData.buildSuccess(linkGroupVo);
    }

    /**
     * 查询用户的全部分组
     */
    @GetMapping("list")
    public JsonData findUserAllLinkGroup() {
        List<LinkGroupVo> list = linkGroupService.findUserAllLinkGroup();
        return JsonData.buildSuccess(list);
    }

    /**
     * 编辑分组
     */
    @PutMapping("update")
    public JsonData update(@RequestBody LinkGroupUpdateRequest request) {
        // 校验分组是否已经存在
        if (linkGroupService.checkGroupExists(request.getTitle())) {
            return JsonData.buildError("分组名称已存在");
        }

        int updateRow = linkGroupService.updateById(request);
        if(updateRow <= 0) {
            return JsonData.buildError("更新失败");
        }
        return JsonData.buildSuccess("更新成功");
    }


}

