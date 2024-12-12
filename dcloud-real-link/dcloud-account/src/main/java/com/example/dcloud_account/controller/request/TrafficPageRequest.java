package com.example.dcloud_account.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/12/12 11:11
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficPageRequest {
    private int page;
    private int size;
}
