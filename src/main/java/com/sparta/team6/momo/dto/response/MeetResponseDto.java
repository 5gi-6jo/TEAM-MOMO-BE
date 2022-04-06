package com.sparta.team6.momo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeetResponseDto {
    private Long planId;
    private String planeName;
}
