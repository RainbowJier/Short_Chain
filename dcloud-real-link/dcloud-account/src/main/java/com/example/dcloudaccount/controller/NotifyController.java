package com.example.dcloudaccount.controller;

import com.example.dcloudaccount.service.NotifyService;
import com.example.dcloudcommon.util.JsonData;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/8/29 21:28
 */
@RestController
@RequestMapping("/api/account/v1")
@Slf4j
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private Producer captchaProducer;


    /**
     * Get captcha image.
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request,HttpServletResponse response) {
        String captchaText = captchaProducer.createText();
        log.info("Captcha text：{}", captchaText);

        // Store the captcha text in the redis, and set expire time. todo

        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);
        try {
            // Get output stream.
            ServletOutputStream outputStream = response.getOutputStream();

            // Write image data to output stream.
            ImageIO.write(bufferedImage, "jpg", outputStream);

            // Close output stream.
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("Failed to get output stream: {}", e.getMessage());
        }
    }


    @RequestMapping("/notify")
    public JsonData notify(String message) {
        notifyService.testNotify();
        return JsonData.buildSuccess();
    }

}
