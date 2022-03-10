package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Plan;

import javax.persistence.*;

@Getter
@NoArgsConstructor
public class ImageDto {
    private String image;

    public ImageDto(String image) {
        this.image = image;
    }


}
