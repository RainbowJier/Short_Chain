package com.example.dcloud_account.controller.request;

import lombok.Data;


@Data
public class UseTrafficRequest {
    private Long accountNo;

    /**
     * 业务id, 短链码
     */
    private String bizId;

}
