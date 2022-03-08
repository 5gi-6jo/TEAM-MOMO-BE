package sparta.team6.momo.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakePlanResponse {
    private Long postId;

    public MakePlanResponse(Long postId) {
        this.postId = postId;
    }
}
