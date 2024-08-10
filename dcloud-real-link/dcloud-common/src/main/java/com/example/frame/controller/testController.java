package com.example.frame.controller;

import com.example.frame.annotation.Systemlog;
import com.example.frame.domain.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/6/15 10:36
 */

@RestController("test")

public class testController {

    @Systemlog(businessName = "测试接口")
    @PostMapping("post")
    public ResponseResult get() {
        return null;
    }
}
