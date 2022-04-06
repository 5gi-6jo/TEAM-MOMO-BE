package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.response.RecordResponseDto;
import com.sparta.team6.momo.dto.response.Success;
import com.sparta.team6.momo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sparta.team6.momo.service.RecordService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;
    private final AccountUtils accountUtils;

    @GetMapping
    public ResponseEntity<Success<List<RecordResponseDto>>> showRecord(@RequestParam("pageNumber") Long pageNumber) {
        List<RecordResponseDto> dtoList = recordService.showRecord(pageNumber, accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }
}
