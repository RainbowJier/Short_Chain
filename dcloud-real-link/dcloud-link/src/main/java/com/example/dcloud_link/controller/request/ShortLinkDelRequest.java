package com.example.dcloud_link.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description：delete short link request.
 * @Author： RainbowJier
 * @Data： 2024/11/3 15:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkDelRequest {
    /**
     * 分组
     */
    private Long groupId;

    /**
     * table group_code_mapping id.
     */
    private Long mappingId;

    /**
     * short link code.
     */
    private String code;
}
