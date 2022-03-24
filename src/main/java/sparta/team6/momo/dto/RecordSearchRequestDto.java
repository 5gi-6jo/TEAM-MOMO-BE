package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RecordSearchRequestDto {

    private Long pageNumber = 0L;

    @NotBlank(message = "검색어를 입력해주세요")
    private String word;

}
