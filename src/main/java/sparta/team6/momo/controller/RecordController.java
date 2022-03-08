package sparta.team6.momo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.PagingDto;
import sparta.team6.momo.dto.ShowRecordResponseDto;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.service.PlanService;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    private PlanService planService;

    @Autowired
    public RecordController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/list")
    public ResponseEntity<Object> showRecord(@RequestParam int pageNumber) {
        List<ShowRecordResponseDto> dtoList = planService.showRecord(pageNumber);
        return ResponseEntity.ok().body(new PagingDto<>(dtoList));
    }
}
