package sparta.team6.momo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sparta.team6.momo.dto.DeleteImageRequestDto;
import sparta.team6.momo.dto.ImageDto;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.service.FileUploadService;

import java.util.List;

@RestController
public class FileUploadController {

    private FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/plans/{planId}/images")
    public ResponseEntity<Object> uploadImage(@RequestParam("files") List<MultipartFile> files, @PathVariable Long planId) {
        fileUploadService.uploadImage(files, planId);
        return ResponseEntity.ok().body(new Success<>("업로드 성공"));
    }

    @GetMapping("/plans/{planId}/images")
    public ResponseEntity<Object> showImage(@PathVariable Long planId) {
        List<ImageDto> imageList = fileUploadService.showImage(planId);
        return ResponseEntity.ok().body(new Success<>("이미지 조회 완료", imageList));
    }

    @DeleteMapping("/plans/images")
    public ResponseEntity<Object> deleteImageS3(@RequestBody DeleteImageRequestDto requestDto) {
        fileUploadService.deleteImageS3(requestDto.getImageId());
        return ResponseEntity.ok().body(new Success<>("이미지 삭제 완료"));
    }
}