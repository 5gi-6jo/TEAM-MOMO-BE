package sparta.team6.momo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.annotation.DTOValid;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.exception.DefaultException;
import sparta.team6.momo.service.PlanService;
import sparta.team6.momo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private PlanService planService;
    private UserService userService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    @DTOValid
    public ResponseEntity<Object> makePlan(@Valid @RequestBody MakePlanRequestDto requestDto, Authentication authentication, BindingResult bindingResult) {
        String email = authentication.getName();
        MakePlanResponseDto responseDto = planService.savePlan(requestDto, email);
        return ResponseEntity.ok().body(new Success<>("생성 완료", responseDto));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId);
        return ResponseEntity.ok().body(new Success<>("삭제 완료"));
    }

    @PutMapping("/{planId}")
    @DTOValid
    public ResponseEntity<Object> updatePlan(@PathVariable Long planId, @Valid @RequestBody UpdatePlanRequestDto requestDto, BindingResult bindingResult) {
        planService.updatePlan(planId, requestDto);
        return ResponseEntity.ok().body(new Success<>("수정 완료"));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<Object> showDetail(@PathVariable Long planId) {
        ShowDetailResponseDto responseDto = planService.showDetail(planId);
        return ResponseEntity.ok().body(new Success<>("조회 완료", responseDto));
    }

    @GetMapping("/main")
    public ResponseEntity<Object> showMain() {
        List<ShowMainResponseDto> dtoList = planService.showMain();
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }
}
