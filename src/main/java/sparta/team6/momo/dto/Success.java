package sparta.team6.momo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class Success<T> {
    private boolean success;
    private String message;
    private List<T> data;

    public Success() {
        success = true;
        message = "Success";
        data = List.of();
    }

    public Success(String message) {
        success = true;
        this.message = message;
        data = List.of();
    }

    public Success(String message, T data) {
        success = true;
        this.message = message;
        this.data = List.of(data);
    }

}

