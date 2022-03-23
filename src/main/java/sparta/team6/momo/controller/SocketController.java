package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import sparta.team6.momo.dto.ChatDto;
import sparta.team6.momo.dto.EnterDto;
import sparta.team6.momo.dto.MapDto;
import sparta.team6.momo.service.SocketService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SocketService socketService;

    @MessageMapping("/enter") // maps/enter
    public void enter(@Payload EnterDto enterDto, SimpMessageHeaderAccessor headerAccessor) {
        ChatDto chatDto = ChatDto.from(enterDto);
        chatDto.setContent(chatDto.getSender() + "님이 입장하셨습니다");

//        Map<String, Object> attributes = headerAccessor.getSessionAttributes();
//        if (attributes != null) {
//            attributes.put("nickname", chatDto.getSender());
//            attributes.put("planId", chatDto.getPlanId());
//        }
        //TODO 목적지 위도 경도 세팅
        log.info("enter로 접속하였습니다!");
        MapDto mapDto = MapDto.from(enterDto);
        simpMessagingTemplate.convertAndSend("/topic/chat/1", chatDto);
        simpMessagingTemplate.convertAndSend("/topic/map/1", mapDto);
    }
//
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

    @MessageMapping("/map.send") // maps/map.send
    public void sendMap(@Payload MapDto mapDto) {
        log.info("info");
        simpMessagingTemplate.convertAndSend("/topic/map/1", mapDto);
    }

    @MessageMapping("/chat.send") // maps/chat.send
    public void sendChat(@Payload ChatDto chatDto) {
        simpMessagingTemplate.convertAndSend("/topic/chat/1", chatDto);
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
}
