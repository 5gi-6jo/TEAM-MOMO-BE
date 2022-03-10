package sparta.team6.momo.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileUploadService {

    private UploadService uploadService;
    private ImageRepository imageRepository;
    private PlanRepository planRepository;

    @Autowired
    public FileUploadService(UploadService uploadService, ImageRepository imageRepository, PlanRepository planRepository) {
        this.uploadService = uploadService;
        this.imageRepository = imageRepository;
        this.planRepository = planRepository;
    }

    //Multipart를 통해 전송된 파일을 업로드하는 메서드
    public void uploadImage(MultipartFile file, Long planId) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        Optional<Plan> plan = planRepository.findById(planId);
        if (plan.isPresent()) {
            try (InputStream inputStream = file.getInputStream()) {
                uploadService.uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
            }
            Image image = new Image(plan.get(), uploadService.getFileUrl(fileName));
            imageRepository.save(image);
        } else {
            throw new CustomException(ErrorCode.PLAN_NOT_FOUND);
        }
    }

    // 기존 확장자명을 유지한 채, 유니크한 파일의 이름을 생성하는 메서드
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    // 파일의 확장자명을 가져오는 메서드
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }
    }

    public List<ImageDto> showImage(Long planId) {
        List<Image> imageList = imageRepository.findAllByPlan_Id(planId);
        List<ImageDto> dtoList = new ArrayList<>();
        for (Image image : imageList) {
            dtoList.add(new ImageDto(image.getImage()));
        }
        return dtoList;
    }

}
