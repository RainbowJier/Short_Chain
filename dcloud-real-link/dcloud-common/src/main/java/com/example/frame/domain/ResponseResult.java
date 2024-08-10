package com.example.frame.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author:Rainbow
 * @CreateTime:2023/12/815:59
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_SUCCESS_MSG = "success";
    private static final String DEFAULT_ERROR_MSG = "An error occurred";
    private static final int DEFAULT_SUCCESS_CODE = 200;
    private static final int DEFAULT_ERROR_CODE = 500;

    private Integer code;
    private String msg;
    private T data;


    // Helper method to set common properties
    private static <T> ResponseResult<T> build(Integer code, String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static <T> ResponseResult<T> ok(T data) {
        return build(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, data);
    }

    public static <T> ResponseResult<T> ok(String msg) {
        return build(DEFAULT_SUCCESS_CODE, msg, null);
    }

    public static <T> ResponseResult<T> ok(String msg, T data) {
        return build(DEFAULT_SUCCESS_CODE, msg, data);
    }

    public static <T> ResponseResult<T> ok(Integer code, String msg, T data) {
        return build(code, msg, data);
    }


    public static <T> ResponseResult<T> error(T data) {
        return build(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG, data);
    }

    public static <T> ResponseResult<T> error(String msg) {
        return build(DEFAULT_ERROR_CODE, msg, null);
    }

    public static <T> ResponseResult<T> error(String msg, T data) {
        return build(DEFAULT_ERROR_CODE, msg, data);
    }

    public static <T> ResponseResult<T> error(Integer code, String msg, T data) {
        return build(code, msg, data);
    }
}