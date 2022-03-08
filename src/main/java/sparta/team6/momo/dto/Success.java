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
        message = "success";
        data = List.of();
    }

    public Success(T data) {
        success = true;
        message = "success";
        this.data = List.of(data);
    }

}

