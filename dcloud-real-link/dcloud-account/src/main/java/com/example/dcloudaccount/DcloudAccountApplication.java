package com.example.dcloudaccount;

import com.example.dcloudaccount.component.SmsComponent;
import com.example.dcloudaccount.config.SmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;


@ComponentScan(basePackages  = {"com.example.dcloudcommon","com.example.dcloudaccount"})
@EnableTransactionManagement  // Open Database Transaction.
@EnableFeignClients          // Enable OpenFeign
@EnableDiscoveryClient      // Enable Service register and discovery.
@SpringBootApplication
@Slf4j
@EnableAsync     // Enable asynchronous method.
public class DcloudAccountApplication {

    public static final String BANNER = "\n" +
            "   _____   _    _    _____    _____   ______    _____    _____ \n" +
            "  / ____| | |  | |  / ____|  / ____| |  ____|  / ____|  / ____|\n" +
            " | (___   | |  | | | |      | |      | |__    | (___   | (___  \n" +
            "  \\___ \\  | |  | | | |      | |      |  __|    \\___ \\   \\___ \\ \n" +
            "  ____) | | |__| | | |____  | |____  | |____   ____) |  ____) |\n" +
            " |_____/   \\____/   \\_____|  \\_____| |______| |_____/  |_____/ \n" +
            "                                                               \n" +
            "                                                               \n";

    public static void main(String[] args) {
        SpringApplication.run(DcloudAccountApplication.class, args);
        log.info(BANNER);
    }
}
