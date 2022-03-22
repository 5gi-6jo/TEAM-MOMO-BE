package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import sparta.team6.momo.dto.ChatDto;
import sparta.team6.momo.dto.MapDto;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/enterMap")
    public void enterMap(@Payload MapDto mapDto) {
        //TODO 목적지 위도 경도 세팅
        simpMessagingTemplate.convertAndSend("topic/map/" + mapDto.getPlanId(), mapDto);
    }

    @MessageMapping("/enterChat")
    public void enterChat(@Payload ChatDto chatDto) {
        chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다");
        simpMessagingTemplate.convertAndSend("topic/chat/" + chatDto.getPlanId(), chatDto);
    }

    @MessageMapping("/sendMap")
    public void sendMap(@Payload MapDto mapDto) {
        simpMessagingTemplate.convertAndSend("topic/map/" + mapDto.getPlanId(), mapDto);
    }

    @MessageMapping("/sendChat")
    public void sendChat(@Payload ChatDto chatDto) {
        simpMessagingTemplate.convertAndSend("topic/chat/" + chatDto.getPlanId(), chatDto);
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
}
