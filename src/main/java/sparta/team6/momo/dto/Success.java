package sparta.team6.momo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class Success<T> {
    private boolean success;
    private String message;
    private final List<T> data;

    public Success() {
        success = true;
        message = "success";
        data = List.of();
    }

    public Success(T data) {
        success = true;
        message = "success";
        this.data = List.of(data);
    }


}
