package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.annotation.DTOValid;
import com.sparta.team6.momo.dto.RecordRequestDto;
import com.sparta.team6.momo.dto.RecordResponseDto;
import com.sparta.team6.momo.dto.RecordSearchRequestDto;
import com.sparta.team6.momo.dto.Success;
import com.sparta.team6.momo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.sparta.team6.momo.service.RecordService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;
    private final AccountUtils accountUtils;

    @PostMapping
    public ResponseEntity<Object> showRecord(@RequestBody RecordRequestDto requestDto) {
        List<RecordResponseDto> dtoList = recordService.showRecord(requestDto.getPageNumber(), requestDto.getPeriod(), accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }

    @PostMapping("/search")
    @DTOValid
    public ResponseEntity<Object> searchRecord(@Valid @RequestBody RecordSearchRequestDto requestDto, BindingResult bindingResult) {
        List<RecordResponseDto> dtoList = recordService.searchRecord(requestDto.getWord(), requestDto.getPageNumber(), accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }

}
