package com.zhaojf.pinganclient;

import com.zhaojf.pinganclient.task.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PingAnClientApplication {

    public static void main(String[] args) {
        if(args.length > 0){
            Task.sleepMillis = Long.parseLong(args[0]);
        }
        SpringApplication springApplication = new SpringApplication(PingAnClientApplication.class);
        ConfigurableApplicationContext run = springApplication.run(args);
        final Task task = run.getBean(Task.class);
        Runtime.getRuntime().addShutdownHook(new Thread(task::stopTask));


    }

}
