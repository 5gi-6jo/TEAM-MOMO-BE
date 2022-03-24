package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmRequestDto {
    private String targetToken;
//    private List<String> tokenList;
    private String title="모두모여(Momo)";
    private String body="약속 시간이 다가오고 있습니다!!";
    private String path;
}
