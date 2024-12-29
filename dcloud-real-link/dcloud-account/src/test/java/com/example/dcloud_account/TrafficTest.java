package com.example.dcloud_account;

import com.example.dcloud_account.manager.TrafficManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *@Description：TODO
 *@Author： RainbowJier
 *@Data： 2024/12/14 21:53
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DcloudAccountApplication.class)
public class TrafficTest {

    @Autowired
    private TrafficManager trafficManager;

    /**
     * 连接 shardingsphere 数据库，测试分库分表功能
     */
    //@Test
    //public void testDeleteExpireTraffic() {
    //    trafficManager.deleteExpiredTraffic(trafficList);
    //}
}
