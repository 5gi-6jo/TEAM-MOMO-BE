package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.annotation.DTOValid;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.exception.DefaultException;
import sparta.team6.momo.service.PlanService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    /* @Valid 파라미터 바로 뒤에 무조건 BindingResult 파라미터가 위치해야함 */
    @PostMapping
    @DTOValid
    public ResponseEntity<Object> makePlan(@Valid @RequestBody MakePlanRequestDto requestDto, BindingResult bindingResult, Authentication authentication) {     
//        Long userId = authentication.getName();
        String email = authentication.getName();
        Long planId = planService.savePlan(requestDto, email);
        return ResponseEntity.ok().body(new Success<>("생성 완료", planId));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long planId, Authentication authentication) {
        String email = authentication.getName();
        planService.deletePlan(planId, email);
        return ResponseEntity.ok().body(new Success<>("삭제 완료"));
    }

    @PutMapping("/{planId}")
    @DTOValid
    public ResponseEntity<Object> updatePlan(@PathVariable Long planId, @Valid @RequestBody MakePlanRequestDto requestDto, BindingResult bindingResult, Authentication authentication) {
        String email = authentication.getName();
        planService.updatePlan(planId, requestDto);
        return ResponseEntity.ok().body(new Success<>("수정 완료", planId));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<Object> showDetail(@PathVariable Long planId, Authentication authentication) {
        String email = authentication.getName();
        ShowDetailResponseDto responseDto = planService.showDetail(planId, email);
        return ResponseEntity.ok().body(new Success<>("조회 완료", responseDto));
    }

    @GetMapping("/main")
    public ResponseEntity<Object> showMain(Authentication authentication) {
        String email = authentication.getName();
        List<ShowMainResponseDto> dtoList = planService.showMain(email);
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }
}
