package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.PagingDto;
import sparta.team6.momo.dto.ShowRecordResponseDto;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.service.PlanService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final PlanService planService;

    //TODO: period별로 data 조회 -> getName() userId로 변경되면 수정할 것

    @GetMapping("/list")
    public ResponseEntity<Object> showRecord(@RequestParam Long pageNumber, @RequestBody Long period, Authentication authentication) {
        String email = authentication.getName();
        List<ShowRecordResponseDto> dtoList = planService.showRecord(pageNumber, period, email);
        return ResponseEntity.ok().body(new PagingDto<>("조회 완료", dtoList));
    }
}
