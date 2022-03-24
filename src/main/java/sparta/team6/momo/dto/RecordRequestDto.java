package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class RecordRequestDto {

    private Long pageNumber = 0L;

    private Long period = 365L;
}
