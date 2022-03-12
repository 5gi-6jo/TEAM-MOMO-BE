package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenReissueDto {
    private String accessToken;
    private String refreshToken;
}
