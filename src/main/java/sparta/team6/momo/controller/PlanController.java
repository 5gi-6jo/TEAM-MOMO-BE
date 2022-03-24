package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.annotation.DTOValid;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.PlanRepository;
import sparta.team6.momo.service.PlanService;
import sparta.team6.momo.utils.AccountUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;
    private final AccountUtils accountUtils;

    //test용
    private final PlanRepository planRepository;


    /* @Valid 파라미터 바로 뒤에 무조건 BindingResult 파라미터가 위치해야함 */
    @PostMapping
    @DTOValid
    public ResponseEntity<Object> makePlan(@Valid @RequestBody MakePlanRequestDto requestDto, BindingResult bindingResult) {
        Long planId = planService.savePlan(requestDto, accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("생성 완료", planId));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId, accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("삭제 완료"));
    }

    @PutMapping("/{planId}")
    @DTOValid
    public ResponseEntity<Object> updatePlan(@PathVariable Long planId, @Valid @RequestBody MakePlanRequestDto requestDto, BindingResult bindingResult) {
        planService.updatePlan(planId, requestDto, accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("수정 완료", planId));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<Object> showDetail(@PathVariable Long planId) {
        ShowDetailResponseDto responseDto = planService.showDetail(planId, accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("조회 완료", responseDto));
    }

    @PostMapping("/main")
    @DTOValid
    public ResponseEntity<Object> showMain(@Valid @RequestBody ShowMainRequestDto requestDto, BindingResult bindingResult) {
        List<ShowMainResponseDto> dtoList = planService.showMain(requestDto.getDate(), accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("조회 완료", dtoList));
    }

    @GetMapping("/test")
    public String test() {
        return planRepository.findById(6L).get().getUrl();
    }

}
