package sparta.team6.momo.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sparta.team6.momo.amazonS3.UploadService;
import sparta.team6.momo.dto.ImageDto;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;
import sparta.team6.momo.model.Image;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.ImageRepository;
import sparta.team6.momo.repository.PlanRepository;

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
    public List<ImageDto> uploadImage(List<MultipartFile> files, Long planId, Long userId) {
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
            if (plan.isPresent() && userId.equals(plan.get().getUser().getId())) {
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

    public List<ImageDto> showImage(Long planId, Long userId) {
        List<Image> imageList = imageRepository.findAllByPlan_Id(planId);
        try {
            if (userId.equals(imageList.get(0).getPlan().getUser().getId())) {
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

    public void deleteImageS3(Long imageId, Long userId) {
        Optional<Image> image = imageRepository.findById(imageId);
        if (image.isPresent() && userId.equals(image.get().getPlan().getUser().getId())) {
            uploadService.deleteFile(image.get().getImage().split(".com/")[1]);
            imageRepository.deleteById(imageId);
        } else {
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }
    }
}
