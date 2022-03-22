package sparta.team6.momo.dto;

import lombok.Data;
import sparta.team6.momo.model.MessageType;

@Data
public class ChatDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private String message;
}
