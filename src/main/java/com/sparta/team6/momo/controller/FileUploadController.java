package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.Success;
import com.sparta.team6.momo.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sparta.team6.momo.dto.ImageDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/plans/{planId}/images")
    public ResponseEntity<Object> uploadImage(@RequestParam("files") List<MultipartFile> files, @PathVariable Long planId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ImageDto> imageDtoList = fileUploadService.uploadImage(files, planId, userId);
        return ResponseEntity.ok().body(new Success<>("업로드 성공", imageDtoList));
    }

    @GetMapping("/plans/{planId}/images")
    public ResponseEntity<Object> showImage(@PathVariable Long planId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ImageDto> imageList = fileUploadService.showImage(planId, userId);
        return ResponseEntity.ok().body(new Success<>("이미지 조회 완료", imageList));
    }

    @DeleteMapping("/plans/images/{imageId}")
    public ResponseEntity<Object> deleteImageS3(@PathVariable Long imageId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        fileUploadService.deleteImageS3(imageId, userId);
        return ResponseEntity.ok().body(new Success<>("이미지 삭제 완료"));
    }
}