package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.exception.DefaultException;
import sparta.team6.momo.service.PlanService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<Object> showRecord(@RequestBody ShowRecordRequestDto requestDto, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ShowRecordResponseDto> dtoList = planService.showRecord(requestDto.getPageNumber(), requestDto.getPeriod(), userId);
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> searchRecord(@Valid @RequestBody SearchRecordRequestDto requestDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new DefaultException(HttpStatus.BAD_REQUEST, bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        Long userId = Long.parseLong(authentication.getName());
        List<ShowRecordResponseDto> dtoList = planService.searchRecord(requestDto.getWord(), requestDto.getPageNumber(), userId);
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }

}
