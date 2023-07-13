package com.zhaojf.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RedisPubController {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisPubController(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @GetMapping("/pub/{channel}")
    public String getUserById(@PathVariable("channel") String channel) {
        //TODO 执行主业务
        new Thread(() -> {
            int i = 10000;
            System.out.println(System.currentTimeMillis());
            while (i-- > 0) {
                redisTemplate.convertAndSend(channel, System.currentTimeMillis());
            }
            System.out.println(System.currentTimeMillis());
            System.out.println("发送完成！！！！！");
        }).start();

        return "Success";
    }

}
