package com.zhaojf.pingan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PingAnApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PingAnApplication.class);
        springApplication.run(args);
    }

}
