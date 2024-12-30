package com.example.dcloud_link.config;

import com.example.dcloud_common.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎拦截器
 * @Date: 2024/10/5 20:52
 * @Version: 1.0
 */

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/api/link/*/**","/api/group/*/**","/api/domain/*/**")
                .excludePathPatterns("/api/link/*/check");
    }
}
