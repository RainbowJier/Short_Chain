package com.example.dcloudshop;

import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_shop.ShopApplication;
import com.example.dcloud_shop.Manager.ProductOrderManager;
import com.example.dcloud_shop.model.entity.ProductOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
class DcloudShopApplicationTests {

    @Autowired
    private ProductOrderManager productOrderManager;

    /**
     * 测试商新增商品订单
     */
    @Test
    void testAdd() {
        for(int i=0;i<10;i++){
            ProductOrder productOrder = ProductOrder.builder()
                    .outTradeNo(CommonUtil.generateUUID())
                    .payAmount(new BigDecimal(11))
                    .state("NEW")
                    .nickname("小滴课堂-老王")
                    .accountNo(1728131003120L)
                    .del(0)
                    .productId(2L)
                    .build();
            productOrderManager.add(productOrder);
        }

    }

    /**
     * 测试分页接口
     */
    @Test
    void testPage(){
        productOrderManager.page(1, 10,1728131003120L,null);

    }














}
