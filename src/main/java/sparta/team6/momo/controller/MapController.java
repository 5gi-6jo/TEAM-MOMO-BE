package sparta.team6.momo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import sparta.team6.momo.dto.LocationDto;

@Controller
public class MapController {

    @MessageMapping("/hello")
    @SendTo("/topic/public")
    public LocationDto sendLocation(@Payload LocationDto locationDto) {
        return locationDto;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public LocationDto sendMessage(@Payload LocationDto chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public LocationDto addUser(@Payload LocationDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getTitle());
        return chatMessage;
    }
}


