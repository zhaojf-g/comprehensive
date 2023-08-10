package com.zhaojf.polardb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PolarApplication {
    public static void main(String[] args) {
        new SpringApplication(PolarApplication.class).run(args);
    }
}
