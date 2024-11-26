package com.example.dcloud_shop.annotation;

import java.lang.annotation.*;

/**
 * 自定义防重提交
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatSubmit {

    /**
     * 支持两种方式：方法参数、令牌
     */
    enum Type{PARAM,TOKEN};

    /**
     * 加锁过期时间，默认是5秒
     */
    long lockTime() default 5;


    /**
     * 默认限制类型，是方法参数
     */
    Type limitType() default Type.PARAM;
}
