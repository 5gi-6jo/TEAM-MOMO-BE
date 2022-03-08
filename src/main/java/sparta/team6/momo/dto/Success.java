package sparta.team6.momo.dto;

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
<<<<<<< HEAD
}
=======
}
>>>>>>> 18b52db519838a44de6904e1d6ff40dd01ebc4e2
