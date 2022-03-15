package sparta.team6.momo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sparta.team6.momo.model.User;

@Getter
@Setter
public class UserResponseDto {
    private Long userId;
    private String email;
    private String nickname;

    @Builder
    public UserResponseDto(Long userId, String email, String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
    }


    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
