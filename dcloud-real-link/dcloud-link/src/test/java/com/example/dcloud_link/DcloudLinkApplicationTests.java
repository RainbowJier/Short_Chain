package com.example.dcloud_link;

import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_link.component.ShortLinkComponent;
import com.example.dcloud_link.entity.ShortLink;
import com.example.dcloud_link.manager.ShortLinkManager;
import com.example.dcloud_link.strategy.ShardingDBConfig;
import com.example.dcloud_link.strategy.ShardingTableConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DcloudLinkApplication.class)
@Slf4j
class DcloudLinkApplicationTests {

    @Autowired
    private ShortLinkComponent shortLinkComponent;

    @Test
    void testDBPrefix() {
        for (int i = 0; i < 20; i++) {
            log.info("randomDBPrefix={}", ShardingTableConfig.getRandomTableSubfix());
        }
    }

    /**
     * 测试murmur哈希算法
     */
    @Test
    void testMurmurHash() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int num1 = random.nextInt(10);
            int num2 = random.nextInt(1000000);
            int num3 = random.nextInt(1000000);

            String originalUrl = num1 + "xdclass" +  num2 + ".net" + num3;

            String linkCode = shortLinkComponent.createShortLinkCode(originalUrl);
            log.info("originalUrl:{}, shortLinkCode={}", originalUrl, linkCode);
        }
    }

    /**
     * 测试短链分库分表存储
     */
    @Autowired
    private ShortLinkManager shortLinkManager;
    @Test
    void testSaveShortLink() {
        Random random = new Random();
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(1000000);
        int num3 = random.nextInt(1000000);

        String originalUrl = num1 + "xdclass" +  num2 + ".net" + num3;
        String linkCode = shortLinkComponent.createShortLinkCode(originalUrl);

        ShortLink shortLink = new ShortLink()
                .setOriginalUrl(originalUrl)
                .setCode(linkCode)
                .setAccountNo((long) num3)
                .setSign(CommonUtil.MD5(originalUrl))
                .setDel(0L);

        shortLinkManager.addShortLink(shortLink);
    }

}
