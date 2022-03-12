package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    private final String accessToken;
    private final String refreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = "Bearer " + accessToken;
        this.refreshToken = refreshToken;
    }
}
