package com.example.dcloud_shop.aspect;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.example.dcloud_common.constant.RedisKey;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.exception.BizException;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_shop.annotation.RepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/11/26 16:16
 * @Version: 1.0
 */


@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 在哪里执行
     * 定义 @Pointcut注解表达式，
     * 方式一：@annotation，当执⾏的⽅法上拥有指定的注解时，生效
     * 方式二：execution，当执行指定的方法时，生效
     */
    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCutNoRepeatSubmit(RepeatSubmit noRepeatSubmit) {}

    /**
     * 执行的操作
     * 环绕通知, 围绕着⽅法执⾏
     *
     * @Around 可以⽤来在调⽤⼀个具体⽅法前和调⽤后来完成⼀些具体的任务。
     * ⽅式⼀：@Around("execution(net.xdclass.controller.*.*(..))")可以
     * ⽅式⼆：用@Pointcut和@Around联合注解也可以（我们采⽤这个）
     */
    @Around("pointCutNoRepeatSubmit(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit noRepeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 限制类型
        String type = noRepeatSubmit.limitType().name();

        // 防重标志
        boolean flag;

        // 方法参数
        if (type.equals(RepeatSubmit.Type.PARAM.name())) {
            long lockTime = noRepeatSubmit.lockTime();  // 加锁时间

            String ipAddr = CommonUtil.getIpAddr(request);   // ip地址

            // 方法名
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String className = method.getDeclaringClass().getName();

            // 构造唯一的key
            String key = String.format("%s-%s-%s-%s", ipAddr, className, method, accountNo);

            // redis实现分布式锁，不存在就设置key，返回true，存在就不设置，返回false
            flag  = redisTemplate.opsForValue().setIfAbsent(key, "1", lockTime, TimeUnit.SECONDS);
        }
        // token 令牌
        else {
            String requestToken = request.getHeader("request-token");

            // 校验令牌
            if (StringUtils.isBlank(requestToken)) {
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_EQUAL_FAIL);
            }

            // 校验令牌是否存在
            String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY, accountNo, requestToken);
            flag = redisTemplate.delete(key);
        }

        // 删除失败，说明重复提交
        if (!flag) {
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_REPEAT);
        }


        System.out.println("⽬标⽅法执⾏前");
        Object object = joinPoint.proceed();
        System.out.println("⽬标⽅法执⾏后");
        return object;
    }
}
