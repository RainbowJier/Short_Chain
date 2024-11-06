package com.example.dcloud_link.service.impl;

import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.controller.request.ShortLinkRequest;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.entity.vo.ShortLinkVo;
import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.service.ShortLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Random;

/**
 * (ShortLink)表服务实现类
 *
 * @author makejava
 * @since 2024-10-29 14:11:16
 */

@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService {
    @Resource
    private ShortLinkManager shortLinkManager;

    @Autowired
    private ShortLinkComponent shortLinkComponent;


    @Override
    public ShortLinkVo parseShortLinkCode(String shortLinkCode) {
        ShortLink shortLink = shortLinkManager.findbyShortLink(shortLinkCode);
        if (shortLink== null) {
            return null;
        }

        // 拷贝属性
        ShortLinkVo shortLinkVo = new ShortLinkVo();
        BeanUtils.copyProperties(shortLink, shortLinkVo);

        return shortLinkVo;
    }
}
