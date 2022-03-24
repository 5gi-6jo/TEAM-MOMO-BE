package sparta.team6.momo.dto;

import lombok.Data;
import sparta.team6.momo.model.MessageType;

@Data
public class EnterDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private Double lat;
    private Double lng;

}
