package com.example.dcloud_link.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.example.dcloud_common.constant.RedisKey;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.enums.ShortLinkStateEnum;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.IDUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.config.RabbitMQConfig;
import com.example.dcloud_link.controller.request.*;
import com.example.dcloud_link.entity.GroupCodeMapping;
import com.example.dcloud_link.entity.LinkGroup;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.entity.vo.ShortLinkVo;
import com.example.dcloud_link.feign.TrafficFeignService;
import com.example.dcloud_link.manager.GroupCodeMappingManager;
import com.example.dcloud_link.manager.LinkGroupManager;
import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.service.ShortLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService {
    @Resource
    private ShortLinkManager shortLinkManager;

    @Resource
    private ShortLinkComponent shortLinkComponent;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMQConfig rabbitMQConfig;

    @Resource
    private LinkGroupManager linkGroupManager;

    @Resource
    private TrafficFeignService trafficFeignService;

    @Resource
    private GroupCodeMappingManager groupCodeMappingManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * B 端-分页查找分组下的短链
     */
    @Override
    public Map<String, Object> page(ShortLinkPageRequest request) {
        Long groupId = request.getGroupId();
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        return groupCodeMappingManager.pageShortLinkByGroupId(request.getPage(), request.getSize(), accountNo, groupId);
    }

    /**
     * parse short link code to get the original url.
     */
    @Override
    public ShortLinkVo parseShortLinkCode(String shortLinkCode) {
        ShortLink shortLink = shortLinkManager.findbyShortLink(shortLinkCode);
        if (shortLink == null) {
            return null;
        }

        ShortLinkVo shortLinkVo = new ShortLinkVo();
        BeanUtils.copyProperties(shortLink, shortLinkVo);

        return shortLinkVo;
    }

    /**
     * add short link and send message to RabbitMQ.
     */
    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        String totalTrafficKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
        // check if the key is existed.
        // if existed, reduce one traffic and check if the number of traffic greater than 0 by using lua script.
        // if not, lua return 0 and the traffic service will calculate the total traffic of the day.
        String script = "if redis.call('get',KEYS[1]) then " +
                "return redis.call('decr',KEYS[1]); " +
                "else return 0; " +
                "end";

        Long leftTrafficTimes = stringRedisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Arrays.asList(totalTrafficKey));

        log.info("【Redis 流量包次数】left traffic times of the day: {}", leftTrafficTimes);

        if (leftTrafficTimes >= 0) {
            // 原始 URL 加上时间戳前缀
            String tameStampUrl = CommonUtil.addUrlPrefix(request.getOriginalUrl());
            request.setOriginalUrl(tameStampUrl);

            // 封装消息对象
            EventMessage eventMessage = EventMessage.builder()
                    .accountNo(accountNo)
                    .content(JsonUtil.objToJsonStr(request))
                    .messageId(IDUtil.generateSnowFlakeID().toString())
                    .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                    .build();

            // send message to create short link.
            rabbitTemplate.convertAndSend(
                    rabbitMQConfig.getShortLinkEventExchange(),   // 交换机名称
                    rabbitMQConfig.getShortLinkAddRoutingKey(),   // 消息对象的 routing key
                    eventMessage                                  // 消息对象
            );

            // respond success to user.
            return JsonData.buildSuccess();
        } else {
            return JsonData.buildResult(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }
    }

    /**
     * delete short link and send message to RabbitMQ.
     */
    @Override
    public JsonData delShortLink(ShortLinkDelRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 封装消息对象
        EventMessage eventMessage = EventMessage.builder()
                .accountNo(accountNo)
                .content(JsonUtil.objToJsonStr(request))
                .messageId(IDUtil.generateSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_DEL_LINK.name())
                .build();

        rabbitTemplate.convertAndSend(
                rabbitMQConfig.getShortLinkEventExchange(),   // 交换机名称
                rabbitMQConfig.getShortLinkDelRoutingKey(),   // 消息对象的 routing key
                eventMessage                                  // 消息对象
        );

        return JsonData.buildSuccess();
    }

    /**
     * update short link and sed the updating message to RabbitMQ.
     */
    @Override
    public JsonData updateShortLink(ShortLinkUpdateRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        EventMessage eventMessage = EventMessage.builder()
                .accountNo(accountNo)
                .content(JsonUtil.objToJsonStr(request))
                .messageId(IDUtil.generateSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_UPDATE_LINK.name())
                .build();

        rabbitTemplate.convertAndSend(
                rabbitMQConfig.getShortLinkEventExchange(),   // 交换机名称
                rabbitMQConfig.getShortLinkUpdateRoutingKey(),   // 消息对象的 routing key
                eventMessage                                  // 消息对象
        );

        return JsonData.buildSuccess();
    }


    /**
     * 处理【新增】短链消息
     * 1. 判断短链域名是否合法（这里不写）
     * 2. 判断组名是否合法
     * 3. 生成长链摘要
     * 4. 生成短链码
     * 5. 加锁
     * 6. 查询短链码是否存在
     * 7. 构建短链对象
     * 8. 保存数据库
     */
    @Override
    public boolean handlerAddShortLink(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();

        String eventMessageType = eventMessage.getEventMessageType();

        ShortLinkAddRequest addRequest = JsonUtil.jsonStrToObj(eventMessage.getContent(), ShortLinkAddRequest.class);

        // domain

        // check if the group name is valid.
        Long groupId = addRequest.getGroupId();
        LinkGroup linkGroup = checkLinkGroup(accountNo, groupId);

        // encrypt OriginalUrl
        String originalUrl = addRequest.getOriginalUrl();
        String originalUrlDigest = CommonUtil.MD5(originalUrl);

        // generate short link code.
        String shortLinkCode = shortLinkComponent.createShortLinkCode(originalUrlDigest);

        // duplicate code.
        boolean duplicateCode = false;

        // lock
        boolean getLock = tryAcquireLock(shortLinkCode, accountNo);

        // lock success
        if (getLock) {
            // C end.
            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equals(eventMessageType)) {
                // 短链码是否存在
                ShortLink shortLinkCodeInDB = shortLinkManager.findbyShortLink(shortLinkCode);
                if (shortLinkCodeInDB != null) {
                    duplicateCode = true;
                    log.error("C 端短链码重复：{}", shortLinkCode);
                } else {
                    boolean reduceFlag = reduceTraffic(eventMessage, shortLinkCode);
                    if (reduceFlag) {
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
                }
            }
            // B END.
            else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equals(eventMessageType)) {
                GroupCodeMapping groupCodeMappingInDB = groupCodeMappingManager.findByCodeAndGroupId(shortLinkCode, groupId, accountNo);
                if (groupCodeMappingInDB != null) {
                    duplicateCode = true;
                    log.error("B 端短链码重复：{}", shortLinkCode);
                } else {
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
            }
        }
        // acquire lock failed.
        else {
            log.error("加锁失败{}", eventMessage);
            // 短链码重复,导致加锁失败
            duplicateCode = true;
        }

        // 短链码重复，重新生成短链码，重新调用方法
        if (duplicateCode) {
            retryOnLockFailure(eventMessage, addRequest);
        }

        return false;
    }

    // 加锁失败后的重试机制
    private void retryOnLockFailure(EventMessage eventMessage, ShortLinkAddRequest addRequest) {
        // 自旋等待
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 重新生成短链码
        String newOriginalUrl = CommonUtil.addUrlPrefixVersion(addRequest.getOriginalUrl());
        addRequest.setOriginalUrl(newOriginalUrl);

        eventMessage.setContent(JsonUtil.objToJsonStr(addRequest));
        log.warn("短链码重复，重新生成短链码：{}", eventMessage);

        // 重新调用方法
        handlerAddShortLink(eventMessage);
    }

    // Acquire lock.
    private boolean tryAcquireLock(String shortLinkCode, Long accountNo) {
        // lua script to acquire lock.
        String script = "if redis.call('EXISTS', KEYS[1]) == 0 then " +
                "redis.call('set', KEYS[1], ARGV[1]); " +
                "redis.call('expire', KEYS[1], ARGV[2]); " +
                "return 1; " +
                "elseif redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return 2; " +
                "else " +
                "return 0; " +
                "end;";

        Long result = stringRedisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Arrays.asList(shortLinkCode), accountNo.toString(), "10");

        return result > 0;
    }

    // check if the group name is valid.
    private LinkGroup checkLinkGroup(Long accountNo, Long groupId) {
        LinkGroup linkGroup = linkGroupManager.detail(groupId, accountNo);
        Assert.notNull(linkGroup, "Group name is not valid.");
        return linkGroup;
    }

    /**
     * reduce traffic
     */
    private boolean reduceTraffic(EventMessage eventMessage, String shortLinkCode) {
        UseTrafficRequest request = UseTrafficRequest.builder()
                .accountNo(eventMessage.getAccountNo()).bizId(shortLinkCode).build();

        // RPC to reduce traffic.
        JsonData jsonData = trafficFeignService.useTraffic(request);

        if (jsonData.getCode() != 0) {
            log.error("流量包不足，扣减失败:{}", eventMessage);
            return false;
        }
        return true;
    }


    @Override
    public boolean handlerDelShortLink(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();

        // 消息类型
        String eventMessageType = eventMessage.getEventMessageType();

        // 解析消息内容
        ShortLinkDelRequest request = JsonUtil.jsonStrToObj(eventMessage.getContent(), ShortLinkDelRequest.class);
        String shortLinkCode = request.getCode();   // 短链码
        Long groupId = request.getGroupId();        // 分组id
        Long mappingId = request.getMappingId();    // 映射表id

        // C端
        if (EventMessageType.SHORT_LINK_DEL_LINK.name().equals(eventMessageType)) {
            ShortLink shortLink = ShortLink.builder()
                    .accountNo(accountNo).code(shortLinkCode)
                    .del(1L).build();

            // 删除短链
            int delRow = shortLinkManager.del(shortLink);
            if (delRow <= 0) {
                log.debug("C 端删除：{}", delRow);
            }
            return true;
        }
        // B端
        else if (EventMessageType.SHORT_LINK_DEL_MAPPING.name().equals(eventMessageType)) {
            GroupCodeMapping groupCodeMapping = GroupCodeMapping.builder()
                    .id(mappingId).accountNo(accountNo)
                    .code(shortLinkCode).groupId(groupId)
                    .del(1L)
                    .build();

            int delRow = groupCodeMappingManager.del(groupCodeMapping);
            if (delRow <= 0) {
                log.debug("B 端删除：{}", delRow);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handlerUpdateShortLink(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();

        // 消息类型
        String eventMessageType = eventMessage.getEventMessageType();

        // 解析消息内容
        ShortLinkUpdateRequest request = JsonUtil.jsonStrToObj(eventMessage.getContent(), ShortLinkUpdateRequest.class);
        Long groupId = request.getGroupId();
        Long mappingId = request.getMappingId();
        String shortLinkCode = request.getCode();
        String title = request.getTitle();    // 短链的标题

        // C端
        if (EventMessageType.SHORT_LINK_UPDATE_LINK.name().equals(eventMessageType)) {
            // 构建短链对象
            ShortLink shortLink = ShortLink.builder()
                    .code(shortLinkCode)
                    .title(title).del(0L)
                    .build();

            shortLinkManager.update(shortLink);
            return true;
        }
        // B端
        else if (EventMessageType.SHORT_LINK_UPDATE_MAPPING.name().equals(eventMessageType)) {
            GroupCodeMapping groupCodeMapping = GroupCodeMapping.builder()
                    .id(mappingId).title(title)
                    .accountNo(accountNo).groupId(groupId)
                    .del(0L).build();

            groupCodeMappingManager.update(groupCodeMapping);
            return true;
        }

        return false;
    }
}













