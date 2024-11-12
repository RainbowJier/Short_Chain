package com.example.dcloud_link.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/11/3 15:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkAddRequest {
    /**
     * 分组
     */
    private Long groupId;
    /**
     * 短链标题
     */
    private String title;
    /**
     * 原始url地址
     */
    private String originalUrl;
    /**
     * 短链域名
     */
    private String domain;
    /**
     * 过期时间，长久就是-1
     */
    private Date expired;
}
