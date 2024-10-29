package com.example.dcloud_account.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/9/22 15:11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCodeRequest {

    /**
     * captcha code.
     */
    private String captcha;

    /**
     * Phone number or email address
     */
    private String to;

}
