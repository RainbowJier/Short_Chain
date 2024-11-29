package com.example.dcloud_common.exception;

import com.example.dcloud_common.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/8/12 21:37
 */

// Response Json Data.
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public JsonData handle(Exception e) {
        // If the exception is sub-exception of RuntimeException.
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            log.info("[业务异常]{}", e.getMessage());
            return JsonData.buildCodeAndMsg(bizException.getCode(), bizException.getMsg());
        } else {
            log.info("[系统异常]{}", e.getMessage());
            return JsonData.buildError("全局异常，未知错误");
        }
    }
}