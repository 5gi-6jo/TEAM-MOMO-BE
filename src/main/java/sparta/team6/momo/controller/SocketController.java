package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import sparta.team6.momo.dto.ChatDto;
import sparta.team6.momo.dto.EnterDto;
import sparta.team6.momo.dto.MapDto;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/enter")
    public void enter(@Payload EnterDto enterDto) {
        ChatDto chatDto = ChatDto.from(enterDto);
        chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다");

        //TODO 목적지 위도 경도 세팅
        MapDto mapDto = MapDto.from(enterDto);
        simpMessagingTemplate.convertAndSend("topic/chat/" + chatDto.getPlanId(), chatDto);
        simpMessagingTemplate.convertAndSend("topic/map/" + mapDto.getPlanId(), mapDto);
    }

//    @MessageMapping("/map.enter")
//    public void enterMap(@Payload MapDto mapDto) {
//        //TODO 목적지 위도 경도 세팅
//        simpMessagingTemplate.convertAndSend("topic/map/" + mapDto.getPlanId(), mapDto);
//    }
//
//    @MessageMapping("/chat.enter")
//    public void enterChat(@Payload ChatDto chatDto) {
//        chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다");
//        simpMessagingTemplate.convertAndSend("topic/chat/" + chatDto.getPlanId(), chatDto);
//    }

    @MessageMapping("/map.send")
    public void sendMap(@Payload MapDto mapDto) {
        simpMessagingTemplate.convertAndSend("topic/map/" + mapDto.getPlanId(), mapDto);
    }

    @MessageMapping("/chat.send")
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
