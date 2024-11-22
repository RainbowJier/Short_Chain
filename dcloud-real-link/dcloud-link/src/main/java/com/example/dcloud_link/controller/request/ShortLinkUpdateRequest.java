package com.example.dcloud_link.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description：目前只能修改短链的标题
 * @Author： RainbowJier
 * @Data： 2024/11/3 15:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkUpdateRequest {
    /**
     * 分组
     */
    private Long groupId;

    /**
     * group_code_mapping 表 id
     */
    private Long mappingId;

    /**
     * 短链码
     */
    private String code;

    /**
     * 短链标题
     */
    private String title;
}
