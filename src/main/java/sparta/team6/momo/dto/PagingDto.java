package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PagingDto<T> {
    private boolean success;
    private String message;
    private List<T> data;
    private boolean isLastPage;

    public PagingDto(String message, T data) {
        success = true;
        this.message = message;
        this.data = List.of(data);
        isLastPage = false;
    }
}
