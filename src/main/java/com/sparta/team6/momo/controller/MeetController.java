package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.Success;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sparta.team6.momo.dto.MeetResponseDto;
import com.sparta.team6.momo.service.MeetService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meets")
public class MeetController {

    private final MeetService meetService;

    @GetMapping("/{randomUrl}") // planId
    public ResponseEntity<?> getPlanIdFromUrl(@PathVariable("randomUrl") String url) {
        Long planId = meetService.getPlanId(url);
        return ResponseEntity.ok().body(new Success<>(new MeetResponseDto(planId)));
    }
}
