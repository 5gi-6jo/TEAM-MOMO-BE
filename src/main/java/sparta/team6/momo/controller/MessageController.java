package sparta.team6.momo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Controller
@ServerEndpoint("/websocket")
public class MessageController extends Socket {
    private static final List<Session> session = new ArrayList<Session>();

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @OnOpen
    public void open(Session newUser) {
        System.out.println("connected");
        session.add(newUser);
        System.out.println(newUser.getId());
    }

    @OnMessage
    public void getMsg(Session recieveSession, String msg) {
        for (Session value : session) {
            if (!recieveSession.getId().equals(value.getId())) {
                try {
                    value.getBasicRemote().sendText("상대 : " + msg);
                } catch (IOException e) {
                    throw new CustomException(ErrorCode.FAILED_TO_SEND_MESSAGE);
                }
            } else {
                try {
                    value.getBasicRemote().sendText("나 : " + msg);
                } catch (IOException e) {
                    throw new CustomException(ErrorCode.FAILED_TO_SEND_MESSAGE);
                }
            }
        }
    }
}
