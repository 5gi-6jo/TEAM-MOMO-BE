package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank(message = "값을 입력해주세요")
    @Email(message = "이메일 형식에 맞지 않습니다")
    private String email;

    @NotBlank(message = "값을 입력해주세요")
    private String nickname;

    @NotBlank(message = "값을 입력해주세요")
    private String password;

    @NotBlank(message = "값을 입력해주세요")
    private String checkPassword;

    @AssertTrue(message = "입력한 비밀번호와 같지 않습니다")
    public boolean isSamePwd() {
        return password.equals(checkPassword);
    }
}
