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

//        @PostMapping("/fcm")
//    public ResponseEntity<Object> pushMessage(@RequestBody FcmRequestDto requestDto) throws IOException {
//        System.out.println(requestDto.getTargetToken() + " " + requestDto.getTitle() + " " + requestDto.getBody());
//
//        firebaseCloudMessageService.sendMessageTo(
//                requestDto.getTargetToken(),
//                requestDto.getTitle(),
//                requestDto.getBody());
//
//        return ResponseEntity.ok().body(new Success<>("전송 완료", requestDto));
//    }
    @PostMapping("/fcm")
    public ResponseEntity<Object> pushMessage(@RequestBody FcmRequestDto requestDto) throws IOException {
        //TODO: 디바이스 토큰 정보를 받는 api. 토큰 정보를 받아서 user db에 저장한다
        firebaseCloudMessageService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody());

        return ResponseEntity.ok().body(new Success<>("전송 완료", requestDto));
    }
}