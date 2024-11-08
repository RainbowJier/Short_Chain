package com.example.dcloud_link.controller;

import com.example.dcloud_common.enums.ShortLinkStateEnum;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_link.entity.vo.ShortLinkVo;
import com.example.dcloud_link.service.ShortLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description：短链重定向，跳转到目标地址
 * @Author： RainbowJier
 * @Data： 2024/11/6 20:07
 */

@RestController
@Slf4j
public class LinkApiController {

    @Autowired
    private ShortLinkService shortLinkService;


    /**
     * 短链重新定向
     * 302 是临时重定向，浏览器会不缓存，每次都问访问后端接口。
     * 301 是永久重定向，浏览器会缓存，导致后端无法统计到短链点击的次数。
     */
    @GetMapping(path = "/{shortLinkCode}")
    public void dispatch(@PathVariable String shortLinkCode,
                         HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("短链码：{}", shortLinkCode);

            // 判断短链码是否合规
            if (isLetterDigit(shortLinkCode)) {
                // 查找短链
                ShortLinkVo shortLinkVo = shortLinkService.parseShortLinkCode(shortLinkCode);

                // 判断是否可以访问
                if (isVisitable(shortLinkVo)) {
                    // 重定向到目标地址
                    String targetUrl = shortLinkVo.getOriginalUrl();
                    response.setHeader("Location", targetUrl);
                    response.setStatus(HttpStatus.FOUND.value());  // 302
                }
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 判断短链是否可用
     */
    private static boolean isVisitable(ShortLinkVo shortLinkVo) {
        if (shortLinkVo == null) {
            return false; // 短链信息为空，直接返回不可用
        }

        long currentTime = CommonUtil.getCurrentTimestamp();
        long expiredTime = shortLinkVo.getExpired().getTime();

        // 有效期内使用
        if (expiredTime > currentTime) {
            String state = shortLinkVo.getState();
            return ShortLinkStateEnum.ACTIVE.name().equalsIgnoreCase(state);
        }

        // 无限使用
        return expiredTime == -1;
    }

    /**
     * 判断字符串是否由字母和数字组成
     */
    private static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }
}
