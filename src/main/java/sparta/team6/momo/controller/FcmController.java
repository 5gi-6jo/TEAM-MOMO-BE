package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.team6.momo.dto.FcmRequestDto;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.service.FirebaseCloudMessageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FcmController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/fcm")
    public ResponseEntity<Object> pushMessage(@RequestBody FcmRequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTargetToken() + " " + requestDto.getTitle() + " " + requestDto.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody(),
                requestDto.getPath());

        return ResponseEntity.ok().body(new Success<>("push message 전송 완료"));
    }
}
