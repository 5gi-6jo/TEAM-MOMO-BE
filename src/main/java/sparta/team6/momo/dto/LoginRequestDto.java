package sparta.team6.momo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
