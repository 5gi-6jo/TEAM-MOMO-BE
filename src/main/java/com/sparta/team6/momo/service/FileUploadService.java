package com.sparta.team6.momo.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import com.sparta.team6.momo.repository.ImageRepository;
import com.sparta.team6.momo.repository.PlanRepository;
import com.sparta.team6.momo.utils.amazonS3.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.sparta.team6.momo.dto.ImageDto;
import com.sparta.team6.momo.model.Image;
import com.sparta.team6.momo.model.Plan;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final UploadService uploadService;
    private final ImageRepository imageRepository;
    private final PlanRepository planRepository;

    //Multipart를 통해 전송된 파일을 업로드하는 메서드
    @Transactional
    public List<ImageDto> uploadImage(List<MultipartFile> files, Long planId, Long accountId) {
        List<ImageDto> imageDtoList = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            String fileName = createFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());
            // S3 storage에 저장
            try (InputStream inputStream = multipartFile.getInputStream()) {
                uploadService.uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
            }
            // DB에 저장
            Optional<Plan> plan = planRepository.findById(planId);
            if (plan.isPresent() && accountId.equals(plan.get().getAccount().getId())) {
                Image image = new Image(plan.get(), uploadService.getFileUrl(fileName));
                imageRepository.save(image);
                imageDtoList.add(new ImageDto(image));
            } else {
                throw new CustomException(ErrorCode.PLAN_NOT_FOUND);
            }
        }
        return imageDtoList;
    }

    // 기존 확장자명을 유지한 채, 유니크한 파일의 이름을 생성하는 메서드
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    // 파일의 확장자명을 가져오는 메서드
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_FILE_FORMAT);
        }
    }

    public List<ImageDto> showImage(Long planId, Long accountId) {
        List<Image> imageList = imageRepository.findAllByPlan_Id(planId);
        try {
            if (accountId.equals(imageList.get(0).getPlan().getAccount().getId())) {
                List<ImageDto> dtoList = new ArrayList<>();
                for (Image image : imageList) {
                    dtoList.add(new ImageDto(image.getId(), image.getImage()));
                }
                return dtoList;
            } else {
                throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }

    }

    public void deleteImageS3(Long imageId, Long accountId) {
        Optional<Image> image = imageRepository.findById(imageId);
        if (image.isPresent() && accountId.equals(image.get().getPlan().getAccount().getId())) {
            uploadService.deleteFile(image.get().getImage().split(".com/")[1]);
            imageRepository.deleteById(imageId);
        } else {
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }
    }
}
