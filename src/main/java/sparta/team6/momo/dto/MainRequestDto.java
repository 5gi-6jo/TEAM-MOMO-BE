package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MainRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

}
