package sparta.team6.momo.dto;

import lombok.*;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import sparta.team6.momo.model.MessageType;

@Getter
@Setter
@NoArgsConstructor
public class ChatDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private String message;

    public static ChatDto from(EnterDto enterDto) {
        return ChatDto.builder()
                .planId(enterDto.getPlanId())
                .type(enterDto.getType())
                .sender(enterDto.getSender())
                .build();
    }

    @Builder
    public ChatDto(Long planId, MessageType type, String sender, String message) {
        this.planId = planId;
        this.type = type;
        this.sender = sender;
        this.message = message;
    }
}
