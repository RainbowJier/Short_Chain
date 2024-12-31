package com.example.dcloud_account.controller;

import com.example.dcloud_account.controller.request.SendCodeRequest;
import com.example.dcloud_account.service.NotifyService;
import com.example.dcloud_common.enums.SendCodeEnum;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.JsonData;
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

@Slf4j
@RestController
@RequestMapping("/api/notify/v1")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Captcha expire time 10 minutes, unit is milliseconds.
     */
    private static final long CAPTCHA_EXPIRE_TIME = 60 * 1000 * 10;

    /**
     * Get captcha image.
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String captchaText = captchaProducer.createText();
        log.info("Captcha text：{}", captchaText);

        redisTemplate.opsForValue().set(getCaptchaKey(request), captchaText, CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);

        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);

            outputStream.flush();
        } catch (IOException e) {
            log.error("Failed to get output stream: {}", e.getMessage());
        }
    }

    public String getCaptchaKey(HttpServletRequest request) {
        String ipAddr = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");

        String key = "account-service:captcha:" + CommonUtil.MD5(ipAddr + userAgent);
        log.info("Captcha key: {}", key);

        return key;
    }

    /**
     * Send SMS code to the specified phone number.
     */
    @PostMapping("/send_code")
    public JsonData notify(@RequestBody SendCodeRequest sendCodeRequest, HttpServletRequest request) {
        // Get captcha text from Redis.
        String key = getCaptchaKey(request);
        String captchaCache = redisTemplate.opsForValue().get(key);

        String captcha = sendCodeRequest.getCaptcha();
        if (captchaCache != null && captcha!=null) {
            // equalsIgnoreCase ignores case.
           if(captchaCache.equalsIgnoreCase(captcha)){
               redisTemplate.delete(key);

               // Send SMS code.
               notifyService.sendCode(SendCodeEnum.USER_REGISTER, sendCodeRequest.getTo());
           }
        }

        return JsonData.buildSuccess();
    }

}
