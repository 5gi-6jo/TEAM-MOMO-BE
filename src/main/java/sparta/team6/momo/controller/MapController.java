package sparta.team6.momo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import sparta.team6.momo.dto.LocationDto;

@Controller
@Slf4j
public class MapController {

    @MessageMapping("/hello")
    @SendTo("/topic/public")
    public LocationDto sendLocation(@Payload LocationDto locationDto) {
        log.info("location connect");
        return locationDto;
    }

}


