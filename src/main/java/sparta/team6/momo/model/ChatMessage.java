package sparta.team6.momo.model;

import lombok.Data;
import sparta.team6.momo.dto.Center;

@Data
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String lat;
    private String lng;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
