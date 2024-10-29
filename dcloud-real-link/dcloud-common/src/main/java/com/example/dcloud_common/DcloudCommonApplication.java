package com.example.dcloud_common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DcloudCommonApplication {

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
        SpringApplication.run(DcloudCommonApplication.class, args);
        log.info(BANNER);
    }

}
