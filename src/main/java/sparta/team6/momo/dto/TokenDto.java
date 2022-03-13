package sparta.team6.momo.dto;

import lombok.Data;

@Data
public class TokenDto {
    private final String accessToken;
    private final String refreshToken;

    public static TokenDto withBearer(String accessToken, String refreshToken) {
        return new TokenDto("Bearer " + accessToken, refreshToken);
    }
}
