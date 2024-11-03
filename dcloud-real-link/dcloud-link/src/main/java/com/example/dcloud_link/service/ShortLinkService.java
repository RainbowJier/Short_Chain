package com.example.dcloud_link.service;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.controller.request.ShortLinkRequest;

/**
 * (ShortLink)表服务接口
 *
 * @author makejava
 * @since 2024-10-29 14:11:16
 */
public interface ShortLinkService {


    JsonData addShortLink(ShortLinkRequest request);
}
