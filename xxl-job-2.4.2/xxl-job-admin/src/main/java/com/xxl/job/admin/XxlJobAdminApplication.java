package com.xxl.job.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */

@Slf4j
@SpringBootApplication
public class XxlJobAdminApplication {
	public static final String BANNER = "\n" +
			"__   ____   ___            _  ____  ____\n"+
			"\\ \\ / /\\ \\ / / |          | |/ __ \\|  _ \\\n"+
			" \\ V /  \\ V /| |          | | |  | | |_) |\n"+
			"  > <    > < | |      _   | | |  | |  _ < \n"+
			" / . \\  / . \\| |____ | |__| | |__| | |_) |\n"+
			"/_/ \\_\\/_/ \\_\\______| \\____/ \\____/|____/ \n";
	public static void main(String[] args) {
        SpringApplication.run(XxlJobAdminApplication.class, args);
		log.info(BANNER);
	}
}