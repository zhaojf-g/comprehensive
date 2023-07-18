package com.zhaojf.pingantest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PingAnTestApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PingAnTestApplication.class);
        springApplication.run(args);
    }

}
