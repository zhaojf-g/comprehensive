package com.zhaojf.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhaojf.vo.Client2ServerMessage;
import com.zhaojf.vo.Server2ClientMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RedisTemplate<Object, Object> redisTemplate;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, RedisTemplate<Object, Object> redisTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    @MessageMapping("/send/{name}") // @MessageMapping 和 @RequestMapping 功能类似，浏览器向服务器发起消息，映射到该地址。
    @SendTo("/nasus/getResponse") // 如果服务器接受到了消息，就会对订阅了 @SendTo 括号中的地址的浏览器发送消息。
    public Server2ClientMessage say(String message, @PathVariable("name") String name) throws Exception {
        return new Server2ClientMessage("Hello," + message + "!");
    }


    @GetMapping("/send")
    public void test() {
        simpMessagingTemplate.convertAndSend("/s2c", "1111");
    }

    @MessageMapping("/c2s")
    public void send(String message) {
        final Client2ServerMessage client2ServerMessage = JSONObject.parseObject(message, Client2ServerMessage.class);

        redisTemplate.convertAndSend("rfid.message." + client2ServerMessage.getTopic(), client2ServerMessage.getMessage());
//        simpMessagingTemplate.convertAndSend("/s2c/" + client2ServerMessage.getTopic(), client2ServerMessage.getMessage());
    }
}
