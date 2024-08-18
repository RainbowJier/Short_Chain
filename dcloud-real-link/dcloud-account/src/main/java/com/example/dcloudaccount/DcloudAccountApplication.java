package com.example.dcloudaccount;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@MapperScan("com.example.dcloudaccount.mapper")
@EnableTransactionManagement  // Open Database Transaction.
@EnableFeignClients          // Enable OpenFeign
@EnableDiscoveryClient      // Enable Service register and discovery.
@SpringBootApplication
public class DcloudAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(DcloudAccountApplication.class, args);
    }

}
