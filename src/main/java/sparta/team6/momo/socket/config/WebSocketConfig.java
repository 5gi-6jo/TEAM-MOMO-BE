package sparta.team6.momo.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // prefix - queue : 1 대 1 통신 , topic : 1 대 N 통신
        registry.enableSimpleBroker("/topic");
        // 데이터 가공 후 메세지 브로커에게 전달해야 할 경우 핸들러를 거쳐야 한다
        // 해당 url 로 접근하면 해당 경로를 처리하고 있는 핸들러로 접근
        registry.setApplicationDestinationPrefixes("/map", "/chat");
    }
}
