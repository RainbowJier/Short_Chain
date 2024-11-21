package com.example.dcloud_link.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@Description：TODO
 *@Author： RainbowJier
 *@Data： 2024/11/21 20:13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkPageRequest {
    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 第几页
     */
    private int page;

    /**
     * 每页多少条
     */
    private int size;
}
