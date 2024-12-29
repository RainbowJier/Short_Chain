package com.example.dcloud_account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ComponentScan(basePackages  = {"com.example.dcloud_account", "com.example.dcloud_common"})
@EnableTransactionManagement  // Open Database Transaction.
@EnableFeignClients          // Enable OpenFeign
@EnableDiscoveryClient      // Enable Service register and discovery.
@SpringBootApplication
@Slf4j
@EnableAsync     // Enable asynchronous method.
public class AccountApplication {

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
        SpringApplication.run(AccountApplication.class, args);
        log.info(BANNER);
    }
}
