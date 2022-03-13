package sparta.team6.momo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class Success<T> {
    private boolean success;
    private String message;
    private final List<T> data;

    public Success() {
        success = true;
        message = "Success";
        data = List.of();
    }


    public Success(T data) {
        success = true;
        message = "success";
        this.data = List.of(data);
    }

    public Success(String message, T data) {
        success = true;
        this.message = message;
        this.data = List.of(data);
    }

    public static Success<TokenDto> tokenDtoSuccess(String jwt) {
        TokenDto token = new TokenDto(jwt);
        return new Success<>(token);
    }
}
}

