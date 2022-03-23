package sparta.team6.momo.dto;

import lombok.*;
import sparta.team6.momo.model.MessageType;

@Getter
@Setter
@NoArgsConstructor
public class ChatDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private String content;

    public static ChatDto from(EnterDto enterDto) {
        return ChatDto.builder()
                .planId(enterDto.getPlanId())
                .type(enterDto.getType())
                .sender(enterDto.getSender())
                .build();
    }

    @Builder
    public ChatDto(Long planId, MessageType type, String sender, String content) {
        this.planId = planId;
        this.type = type;
        this.sender = sender;
        this.content = content;
    }
}
