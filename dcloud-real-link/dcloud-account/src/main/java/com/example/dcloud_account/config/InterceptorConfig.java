package com.example.dcloud_account.config;

import com.example.dcloud_common.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/api/account/*/**","/api/traffic/*/**")
                .excludePathPatterns(
                        "/api/account/*/register","/api/account/*/login",
                        "/api/notify/captcha","/api/notify/send_code",
                        "/api/traffic/*/reduce"
                );
    }
}
