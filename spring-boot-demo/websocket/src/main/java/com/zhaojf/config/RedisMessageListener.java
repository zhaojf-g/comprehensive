package com.zhaojf.config;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageListener implements MessageListener {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisMessageListener(SimpMessagingTemplate simpMessagingTemplate, RedisTemplate<Object, Object> redisTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        // 获取消息
        byte[] messageBody = message.getBody();
        // 使用值序列化器转换
        Object msg = redisTemplate.getValueSerializer().deserialize(messageBody);
        // 获取监听的频道
        byte[] channelByte = message.getChannel();
        // 使用字符串序列化器转换
        String channel = redisTemplate.getStringSerializer().deserialize(channelByte);

        // 发送消息到websocket
        assert channel != null;
        final String[] split = channel.split("\\.");
        simpMessagingTemplate.convertAndSend("/s2c/" + split[split.length - 1], msg);

    }
}
