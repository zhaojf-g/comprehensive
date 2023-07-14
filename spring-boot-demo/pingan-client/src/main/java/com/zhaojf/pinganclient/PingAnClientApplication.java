package com.zhaojf.pinganclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PingAnClientApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PingAnClientApplication.class);
        springApplication.run(args);
    }

}
