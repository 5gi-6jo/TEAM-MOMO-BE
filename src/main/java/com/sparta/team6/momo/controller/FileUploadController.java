package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.Success;
import com.sparta.team6.momo.service.FileUploadService;
import com.sparta.team6.momo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sparta.team6.momo.dto.ImageDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final AccountUtils accountUtils;

    @PostMapping("/plans/{planId}/images")
    public ResponseEntity<Object> uploadImage(@RequestParam("files") List<MultipartFile> files, @PathVariable Long planId) {
        List<ImageDto> imageDtoList = fileUploadService.uploadImage(files, planId, accountUtils.getCurUserId());
        log.info("이미지 업로드 성공");
        return ResponseEntity.ok().body(new Success<>("업로드 성공", imageDtoList));
    }

    @GetMapping("/plans/{planId}/images")
    public ResponseEntity<Object> showImage(@PathVariable Long planId) {
        List<ImageDto> imageList = fileUploadService.showImage(planId, accountUtils.getCurUserId());
        log.info("이미지 조회 성공");
        return ResponseEntity.ok().body(new Success<>("이미지 조회 완료", imageList));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Object> deleteImageS3(@PathVariable Long imageId) {
        fileUploadService.deleteImageS3(imageId, accountUtils.getCurUserId());
        log.info("이미지 삭제 성공");
        return ResponseEntity.ok().body(new Success<>("이미지 삭제 완료"));
    }
}