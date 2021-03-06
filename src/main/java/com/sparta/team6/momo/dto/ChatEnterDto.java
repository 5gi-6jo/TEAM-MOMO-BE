package com.sparta.team6.momo.dto;

import com.sparta.team6.momo.model.MessageType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatEnterDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private String content;
    private List<ChatDto> chats;

    public static ChatEnterDto from(EnterDto enterDto) {
        return ChatEnterDto.builder()
                .planId(enterDto.getPlanId())
                .type(enterDto.getType())
                .sender(enterDto.getSender())
                .build();
    }
}
