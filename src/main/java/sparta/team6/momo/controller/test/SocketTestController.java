package sparta.team6.momo.controller.test;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import sparta.team6.momo.dto.TestSocket;

@Controller
public class SocketTestController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public TestSocket testSocket(@Payload TestSocket testSocket) {
        return testSocket;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public TestSocket addUser(@Payload TestSocket testSocket, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", testSocket.getSender());
        return testSocket;
    }
}
