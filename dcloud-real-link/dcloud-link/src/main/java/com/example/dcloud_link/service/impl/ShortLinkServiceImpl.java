package com.example.dcloud_link.service.impl;

import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.controller.request.ShortLinkRequest;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.service.ShortLinkService;
import lombok.extern.slf4j.Slf4j;
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
    public JsonData addShortLink(ShortLinkRequest request) {
        // 获取当前登录的账号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        Random random = new Random();
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(1000000);
        int num3 = random.nextInt(1000000);

        String originalUrl = num1 + "xdclass" +  num2 + ".net" + num3;
        String linkCode = shortLinkComponent.createShortLinkCode(originalUrl);

        ShortLink shortLink = new ShortLink()
                .setOriginalUrl(originalUrl)
                .setCode(linkCode)
                .setAccountNo(accountNo)
                .setSign(CommonUtil.MD5(originalUrl))
                .setDel(0L);

        int insertCount = shortLinkManager.addShortLink(shortLink);
        if(insertCount <= 0){
            return JsonData.buildError("新增失败");
        }
        return JsonData.buildSuccess("新增成功");
    }
}
