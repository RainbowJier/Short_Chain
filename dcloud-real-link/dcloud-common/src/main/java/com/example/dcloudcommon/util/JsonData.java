package com.example.dcloudcommon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.dcloudcommon.enums.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/8/12 21:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JsonData {
    /**
     * 状态码 0 表示成功
     */
    private Integer code;
    /**
     * 数据
     */
    private Object data;
    /**
     * 描述
     */
    private String msg;

    /**
     * 获取数据内容，并进行类型转换。
     * 注意：这里直接返回数据，不进行序列化和反序列化操作。
     *
     * @param typeReference 类型引用
     * @return 转换后的数据
     */
    public <T> T getData(TypeReference<T> typeReference) {
        return JSON.parseObject(JSON.toJSONString(data), typeReference);
    }

    /**
     * 成功，不传⼊数据
     */
    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    /**
     * 成功，传⼊数据
     */

    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, null);
    }

    /**
     * 失败，传⼊描述信息
     *
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    /**
     * ⾃定义状态码和错误信息
     */
    public static JsonData buildCodeAndMsg(int code, String msg) {
        return new JsonData(code, null, msg);
    }

    /**
     * 传⼊枚举，返回信息
     */
    public static JsonData buildResult(BizCodeEnum codeEnum) {
        return JsonData.buildCodeAndMsg(codeEnum.getCode(), codeEnum.getMessage());
    }
}