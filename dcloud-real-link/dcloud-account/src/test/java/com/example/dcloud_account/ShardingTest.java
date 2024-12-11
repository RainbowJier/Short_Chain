package com.example.dcloud_account;

import com.example.dcloud_account.entity.Traffic;
import com.example.dcloud_account.mapper.TrafficMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎测试ShardingSphere分库分表功能
 * @Date: 2024/10/18 10:55
 * @Version: 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DcloudAccountApplication.class)
public class ShardingTest {

    @Autowired
    private TrafficMapper trafficMapper;

    /**
     * 连接 shardingsphere 数据库，测试分库分表功能
     */
    @Test
    public void testShardingSphere() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Traffic traffic = new Traffic();
            traffic.setAccountNo(Long.valueOf(random.nextInt(100)));
            trafficMapper.insert(traffic);
        }
    }
}
