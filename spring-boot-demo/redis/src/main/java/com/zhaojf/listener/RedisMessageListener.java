package com.zhaojf.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageListener implements MessageListener {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisMessageListener(RedisTemplate<Object, Object> redisTemplate) {
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
        Object channel = redisTemplate.getStringSerializer().deserialize(channelByte);
        // 渠道名称转换
//
        final String s = new String(pattern);
        assert msg != null;
        final long l = Long.parseLong(msg.toString());
        long d = System.currentTimeMillis() - l;

        System.out.println("频道: " + channel + "\t消息内容: " + msg + " \t" + d);

    }
}
