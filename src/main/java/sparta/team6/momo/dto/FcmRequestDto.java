package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmRequestDto {
    private String targetToken;
//    private List<String> tokenList;
    private String title;
    private String body;
}
