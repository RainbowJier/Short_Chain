package com.example.dcloud_link.service;

import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.ShortLinkAddRequest;
import com.example.dcloud_link.controller.request.ShortLinkDelRequest;
import com.example.dcloud_link.controller.request.ShortLinkPageRequest;
import com.example.dcloud_link.controller.request.ShortLinkUpdateRequest;
import com.example.dcloud_link.entity.vo.ShortLinkVo;

import java.util.Map;

/**
 * (ShortLink)表服务接口
 *
 * @author makejava
 * @since 2024-10-29 14:11:16
 */
public interface ShortLinkService {

    /**
     * 解析短链
     */
    ShortLinkVo parseShortLinkCode(String shortLinkCode);

    /**
     * 分页查找分组下的短链
     */
    Map<String, Object> page(ShortLinkPageRequest request);


    // -------------------------- 发送消息到 RabbitMQ  --------------------
    /**
     * 创建短链
     */
    JsonData createShortLink(ShortLinkAddRequest shortLinkRequest);

    /**
     * 删除短链
     */
    JsonData delShortLink(ShortLinkDelRequest request);

    /**
     * 更新短链
     */
    JsonData updateShortLink(ShortLinkUpdateRequest request);


    // -------------------------- 以下为 RabbitMQ 消息处理 --------------------
    /**
     * 处理新增短链消息
     */
    boolean handlerAddShortLink(EventMessage eventMessage);

    /**
     * 删除短链
     */
    boolean handlerDelShortLink(EventMessage eventMessage);

    /**
     * 更新短链
     */
    boolean handlerUpdateShortLink(EventMessage eventMessage);
}
