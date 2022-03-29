package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.Success;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sparta.team6.momo.dto.FcmRequestDto;
import com.sparta.team6.momo.service.FirebaseCloudMessageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FcmController {
    // 배포 시 삭제할 controller 입니다(테스트용)
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/fcm")
    public ResponseEntity<Object> pushMessage(@RequestBody FcmRequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTargetToken() + " " + requestDto.getTitle() + " " + requestDto.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody(),
                requestDto.getUrl());

        return ResponseEntity.ok().body(new Success<>("push message 전송 완료"));
    }
}
