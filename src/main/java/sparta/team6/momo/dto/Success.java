package sparta.team6.momo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Success<T> {
    private final boolean success = true;
    private String message = "success";
    private T data;

    public Success(String message) {
        this.message = message;
    }

    public Success(T data) {
        this.data = data;
    }

    public Success(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static Success<TokenDto> of(TokenDto tokenDto) {
        return new Success<>(tokenDto);
    }

    public static Success<AccountResponseDto> of(AccountResponseDto accountResponseDto) {
        return new Success<>(accountResponseDto);
    }
}

