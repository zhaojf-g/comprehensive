package com.zhaojf.pinganclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor myThreadPoolTaskExecutor() {
        return new ThreadPoolExecutor(100, 10000,
                20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), r -> {
                    final Thread thread = new Thread(r);
                    thread.setPriority(10);
                    return thread;
                });
    }

//    @Bean
//    public ThreadPoolExecutor myThreadPoolTaskExecutor2() {
//        return new ThreadPoolExecutor(100, 10000,
//                20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread();
//            }
//        });
//    }

}
