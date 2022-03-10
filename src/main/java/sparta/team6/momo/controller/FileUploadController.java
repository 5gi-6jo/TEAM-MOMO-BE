package sparta.team6.momo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    //@RequestPart 어노테이션을 이용해서 multipart/form-data 요청을 받을 수 있다
    @PostMapping("/plans/{planId}/images")
    public ResponseEntity<Object> uploadImage(@RequestPart MultipartFile file, @PathVariable Long planId) {
        fileUploadService.uploadImage(file, planId);
        return ResponseEntity.ok().body(new Success<>("업로드 성공"));
    }

    @GetMapping("/plans/{planId}/images")
    public ResponseEntity<Object> showImage(@PathVariable Long planId) {
        List<ImageDto> imageList = fileUploadService.showImage(planId);
        return ResponseEntity.ok().body(new Success<>("이미지 조회 완료", imageList));
    }
}
//Todo: 스토리지에서 이미지 삭제 ( plan 자체가 사라질때도 cascade로 스토리지에서 삭제해야함 )