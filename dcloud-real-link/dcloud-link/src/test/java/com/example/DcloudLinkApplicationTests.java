package com.example;

import com.example.component.ShortLinkComponent;
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

}
