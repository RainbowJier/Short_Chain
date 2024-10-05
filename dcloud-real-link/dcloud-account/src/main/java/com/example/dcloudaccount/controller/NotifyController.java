package com.example.dcloudaccount.controller;

import com.example.dcloudaccount.entity.request.SendCodeRequest;
import com.example.dcloudaccount.service.NotifyService;
import com.example.dcloudcommon.enums.SendCodeEnum;
import com.example.dcloudcommon.util.CommonUtil;
import com.example.dcloudcommon.util.JsonData;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description：获取图形验证码并验证短信验证码
 * @Author： RainbowJier
 * @Data： 2024/8/29 21:28
 */
@RestController
@RequestMapping("/api/notify/v1")
@Slf4j
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final long CAPTCHA_EXPIRE_TIME = 60 * 1000 * 10; // 10 minutes.

    /**
     * Get captcha image.
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String captchaText = captchaProducer.createText();
        log.info("Captcha text：{}", captchaText);

        // Store the captcha text in the Redis, and set expire time.
        redisTemplate.opsForValue().set(getCaptchaKey(request), captchaText, CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);

        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);
        try {
            // Get output stream.
            ServletOutputStream outputStream = response.getOutputStream();

            // Write image data to output stream.
            ImageIO.write(bufferedImage, "jpg", outputStream);

            // Close output stream.
            outputStream.flush();
        } catch (IOException e) {
            log.error("Failed to get output stream: {}", e.getMessage());
        }
    }

    /**
     * 生成验证码key
     */
    public String getCaptchaKey(HttpServletRequest request) {
        // 获取IP
        String ipAddr = CommonUtil.getIpAddr(request);

        // 获取浏览器指纹
        String userAgent = request.getHeader("User-Agent");

        String key = "account-service:captcha:" + CommonUtil.MD5(ipAddr + userAgent);
        log.info("Captcha key: {}", key);

        return key;
    }


    /**
     * 发送短信验证码
     */
    @PostMapping("/send-code")
    public JsonData notify(@RequestBody SendCodeRequest sendCodeRequest, HttpServletRequest request) {

        // Get captcha text from Redis.
        String key = getCaptchaKey(request);
        String captchaCache = redisTemplate.opsForValue().get(key);

        String captcha = sendCodeRequest.getCaptcha();
        if (captchaCache != null && captcha!=null) {
            // equalsIgnoreCase ignores case.
           if(captchaCache.equalsIgnoreCase(captcha)){
               // Delete captcha from Redis.
               redisTemplate.delete(key);

               // Send SMS code.
               notifyService.sendCode(SendCodeEnum.USER_REGISTER, sendCodeRequest.getTo());
           }
        }

        return JsonData.buildSuccess();
    }

}
