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
<<<<<<< HEAD
}
=======
}
>>>>>>> 18b52db519838a44de6904e1d6ff40dd01ebc4e2
