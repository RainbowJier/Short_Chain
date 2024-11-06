package com.example.dcloud_link.service;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.ShortLinkRequest;
import com.example.dcloud_link.entity.vo.ShortLinkVo;

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
}
