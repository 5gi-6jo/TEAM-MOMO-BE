package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.ChatDto;
import com.sparta.team6.momo.dto.ChatEnterDto;
import com.sparta.team6.momo.dto.EnterDto;
import com.sparta.team6.momo.dto.MapDto;
import com.sparta.team6.momo.service.SocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SocketController {

    private final String REDIS_CHAT_KEY = "CHATS";
    private final String REDIS_CHAT_PREFIX = "CHAT";
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SocketService socketService;
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, List<ChatDto>> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @MessageMapping("/enter") // maps/enter
    public void enter(@Payload EnterDto enterDto, SimpMessageHeaderAccessor headerAccessor) {
        ChatEnterDto chatDto = ChatEnterDto.from(enterDto);
        chatDto.setContent(chatDto.getSender() + "님이 입장하셨습니다");

        List<ChatDto> chats = hashOperations.get(REDIS_CHAT_KEY, REDIS_CHAT_PREFIX + enterDto.getPlanId());
        chatDto.setChats(chats);

        MapDto mapDto = MapDto.from(enterDto);
        socketService.setDestination(enterDto.getPlanId(), mapDto);

        simpMessagingTemplate.convertAndSend("/topic/map/" + mapDto.getPlanId(), mapDto);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatDto.getPlanId(), chatDto);
    }


    @MessageMapping("/map.send") // maps/map.send
    public void sendMap(@Payload MapDto mapDto) {
        simpMessagingTemplate.convertAndSend("/topic/map/" + mapDto.getPlanId(), mapDto);
    }


    @MessageMapping("/chat.send") // maps/chat.send
    public void sendChat(@Payload ChatDto chatDto) {
        Long planId = chatDto.getPlanId();

        List<ChatDto> chats = hashOperations.get(REDIS_CHAT_KEY, REDIS_CHAT_PREFIX + planId);
        if (chats == null) {
            chats = new ArrayList<>();
            LocalDateTime expireDate = socketService.getExpireDate(planId);
            Timestamp timestamp = Timestamp.valueOf(expireDate);
            redisTemplate.expireAt(REDIS_CHAT_PREFIX + planId, timestamp);
        }

        chats.add(chatDto);
        hashOperations.put(REDIS_CHAT_KEY, REDIS_CHAT_PREFIX + planId, chats);

        simpMessagingTemplate.convertAndSend("/topic/chat/" + planId, chatDto);
    }
}
