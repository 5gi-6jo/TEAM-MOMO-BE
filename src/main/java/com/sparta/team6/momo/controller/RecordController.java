package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.RecordResponseDto;
import com.sparta.team6.momo.dto.Success;
import com.sparta.team6.momo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sparta.team6.momo.service.RecordService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;
    private final AccountUtils accountUtils;

    @GetMapping
    public ResponseEntity<Object> showRecord(@RequestParam("pageNumber") Long pageNumber) {
        List<RecordResponseDto> dtoList = recordService.showRecord(pageNumber, accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }
}
