package sparta.team6.momo.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MapController {

    @MessageMapping("/hello")
    @SendTo("/topic/greeting")
    public String testConnection(String message) throws InterruptedException {
        Thread.sleep(1000);
        return message;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}

@Getter
@Setter
class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
}

enum MessageType {
    CHAT,
    JOIN,
    LEAVE
}
