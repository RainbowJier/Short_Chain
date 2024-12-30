package com.example.dcloud_account.controller.request;

import lombok.Data;


@Data
public class UseTrafficRequest {
    private Long accountNo;

    /**
     * business id or short link.
     */
    private String bizId;

}
