package sparta.team6.momo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import sparta.team6.momo.dto.LocationDto;

@Controller
public class MapController {

    @MessageMapping("/")
    @SendTo("/topic/public")
    public LocationDto sendMessage(@Payload LocationDto locationDto) {
        return locationDto;
    }
}


