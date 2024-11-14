package com.example.dcloud_link.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.enums.ShortLinkStateEnum;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.IDUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.config.RabbitMQConfig;
import com.example.dcloud_link.controller.request.ShortLinkAddRequest;
import com.example.dcloud_link.entity.GroupCodeMapping;
import com.example.dcloud_link.entity.LinkGroup;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.entity.vo.ShortLinkVo;
import com.example.dcloud_link.manager.GroupCodeMappingManager;
import com.example.dcloud_link.manager.LinkGroupManager;
import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.service.ShortLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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

    @Autowired
    private LinkGroupManager linkGroupManager;

    @Autowired
    private GroupCodeMappingManager groupCodeMappingManager;

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

    /**
     * 发送消息到 RabbitMQ
     */
    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 原始 URL 加上时间戳前缀
        String tameStampUrl = CommonUtil.addUrlPrefix(request.getOriginalUrl());
        request.setOriginalUrl(tameStampUrl);

        // 封装消息对象
        EventMessage eventMessage = EventMessage.builder()
                .accountNo(accountNo)
                .content(JsonUtil.objToJson(request))
                .messageId(IDUtil.generateSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                .build();

        rabbitTemplate.convertAndSend(
                rabbitMQConfig.getShortLinkEventExchange(),   // 交换机名称
                rabbitMQConfig.getShortLinkAddRoutingKey(),   // 消息对象的 routing key
                eventMessage                                  // 消息对象
        );

        return JsonData.buildSuccess();
    }


    /**
     * 处理添加短链事件
     * 1. 判断短链域名是否合法（这里不写）
     * 2. 判断组名是否合法
     * 3. 生成长链摘要
     * 4. 生成短链码
     * 5. 加锁（暂时不写，后续添加）
     * 6. 查询短链码是否存在
     * 7. 构建短链对象
     * 8. 保存数据库
     */
    @Override
    public boolean handlerAddShortLink(EventMessage eventMessage) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 消息类型
        String eventMessageType = eventMessage.getEventMessageType();

        // 解析消息内容
        ShortLinkAddRequest addRequest = JsonUtil.jsonStrToObj(eventMessage.getContent(), ShortLinkAddRequest.class);

        // 域名

        // 校验组名是否合法
        LinkGroup linkGroup = checkLinkGroup(accountNo, addRequest.getGroupId());

        // 生成长链摘要（原始链接加密）
        String originalUrlDigest = CommonUtil.MD5(addRequest.getOriginalUrl());

        // 生成短链码
        String shortLinkCode = shortLinkComponent.createShortLinkCode(originalUrlDigest);

        // todo:加锁

        // 短链码是否存在
        ShortLink shortLinkCodeInDB = shortLinkManager.findbyShortLink(shortLinkCode);
        if (shortLinkCodeInDB != null) {
            return false;
        }

        // C端
        if(EventMessageType.SHORT_LINK_ADD.name().equals(eventMessageType)) {
            // 构建短链对象
            ShortLink shortLink = ShortLink.builder()
                    .accountNo(accountNo)
                    .code(shortLinkCode)
                    .title(addRequest.getTitle())
                    .originalUrl(addRequest.getOriginalUrl())
                    .domain(null)
                    .groupId(linkGroup.getId())
                    .expired(addRequest.getExpired())
                    .sign(originalUrlDigest)
                    .state(ShortLinkStateEnum.ACTIVE.name())
                    .del(0L)
                    .build();

            shortLinkManager.addShortLink(shortLink);
            return true;
        }
        // B端
        else if(EventMessageType.SHORT_LINK_ADD_MAPPING.name().equals(eventMessageType)){
            GroupCodeMapping groupCodeMapping = GroupCodeMapping.builder()
                    .accountNo(accountNo)
                    .code(shortLinkCode)
                    .title(addRequest.getTitle())
                    .originalUrl(addRequest.getOriginalUrl())
                    .domain(null)
                    .groupId(linkGroup.getId())
                    .expired(addRequest.getExpired())
                    .sign(originalUrlDigest)
                    .state(ShortLinkStateEnum.ACTIVE.name())
                    .del(0L)
                    .build();

            groupCodeMappingManager.add(groupCodeMapping);
            return true;
        }

        return false;
    }

    /**
     * 检查组名是否合法
     */
    private LinkGroup checkLinkGroup(Long accountNo, Long groupId) {
        LinkGroup linkGroup = linkGroupManager.detail(groupId, accountNo);
        Assert.notNull(linkGroup, "组名不合法");
        return linkGroup;
    }


}













