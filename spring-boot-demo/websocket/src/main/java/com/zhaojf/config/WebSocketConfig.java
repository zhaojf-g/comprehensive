package com.zhaojf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个 Stomp 的节点(endpoint),并指定使用 SockJS 协议。
        registry.addEndpoint("/rfid/sockjs").addInterceptors(new SessionAuthHandshakeInterceptor()).setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/rfid/socket").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10);
//        registration.interceptors(this.stompChannelInterceptor);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                log.info("--websocket信息发送前--");
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null) {
                    // 判断是否是连接Command 如果是,需要获取token对象
                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        final String token = accessor.getFirstNativeHeader("token");
                        System.out.println(token);
//                        if (!TokenUtil.validateToken(token)) {
//                            return null;
//                        }
//                        final LoginUser user = TokenUtil.getUserFromToken(token);
//                        UserUtil.setUser(user);
//                        // sendToUser 需要与这里的user获取的principal一样
//                        accessor.setUser(new SocketUser(user));
//                        log.info("websocket 连接成功");
                    }
                }
                return message;
            }
        });
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/s2c", "/c2s").setHeartbeatValue(new long[]{10000, 20000}).setTaskScheduler(taskScheduler);
        ;
        // 广播式配置名为 /nasus 消息代理 , 这个消息代理必须和 controller 中的 @SendTo 配置的地址前缀一样或者全匹配
//        registry.enableSimpleBroker("/nasus");
    }

}
