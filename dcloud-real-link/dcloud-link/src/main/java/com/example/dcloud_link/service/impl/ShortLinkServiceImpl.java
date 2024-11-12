package com.example.dcloud_link.service.impl;

import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.EventEnum;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.IDUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.config.RabbitMQConfig;
import com.example.dcloud_link.controller.request.ShortLinkAddRequest;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.entity.vo.ShortLinkVo;
import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.service.ShortLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Override
    public ShortLinkVo parseShortLinkCode(String shortLinkCode) {
        ShortLink shortLink = shortLinkManager.findbyShortLink(shortLinkCode);
        if (shortLink == null) {
            return null;
        }

        // 拷贝属性
        ShortLinkVo shortLinkVo = new ShortLinkVo();
        BeanUtils.copyProperties(shortLink, shortLinkVo);

        return shortLinkVo;
    }

    @Override
    public JsonData createShortLink(ShortLinkAddRequest shortLinkRequest) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 封装消息对象
        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.objToJson(shortLinkRequest))
                .messageId(IDUtil.generateSnowFlakeID().toString())
                .eventMessageType(EventEnum.SHORT_LINK_ADD.name())
                .build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(),   // 交换机名称
                rabbitMQConfig.getShortLinkAddRoutingKey(),             // 消息对象的 routing key
                eventMessage);

        return JsonData.buildSuccess();

    }
}
