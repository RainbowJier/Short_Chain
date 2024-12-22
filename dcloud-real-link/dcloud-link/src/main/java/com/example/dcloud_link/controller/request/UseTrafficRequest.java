package com.example.dcloud_link.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UseTrafficRequest {
    private Long accountNo;

    /**
     * 业务id, 短链码
     */
    private String bizId;


}
