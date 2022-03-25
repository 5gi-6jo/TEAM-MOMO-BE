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
        MapDto mapDto = MapDto.from(enterDto);
        socketService.setDestination(enterDto.getPlanId(), mapDto);
        log.info("enter");

        simpMessagingTemplate.convertAndSend("/topic/map/" + mapDto.getPlanId(), mapDto);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatDto.getPlanId(), chatDto);
    }

    @MessageMapping("/map.send") // maps/map.send
    public void sendMap(@Payload MapDto mapDto) {
        simpMessagingTemplate.convertAndSend("/topic/map/" + mapDto.getPlanId(), mapDto);
    }

    @MessageMapping("/chat.send") // maps/chat.send
    public void sendChat(@Payload ChatDto chatDto) {
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatDto.getPlanId(), chatDto);
    }
}
