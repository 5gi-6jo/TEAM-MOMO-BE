package sparta.team6.momo.dto;

import lombok.Data;

@Data
public class Fail {
    private boolean success;
    private String message;

    public Fail(String errorMessage) {
        success = false;
        message = errorMessage;
    }
}
