package com.example.dcloud_link.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/10/29 19:27
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkGroupUpdateRequest {
    /**
     * 组id
     */
    private Long id;

    /**
     * 组名
     */
    private String title;

}
