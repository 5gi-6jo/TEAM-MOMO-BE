package sparta.team6.momo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.DefaultException;
import sparta.team6.momo.service.PlanService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<Object> makePlan(@Valid @RequestBody MakePlanRequestDto requestDto, Errors errors) {
        if (errors.hasErrors()) {
            for (FieldError error : errors.getFieldErrors()) {
                throw new DefaultException(HttpStatus.BAD_REQUEST, error.getDefaultMessage());
            }
        }
        MakePlanResponseDto responseDto = planService.savePlan(requestDto);
        return ResponseEntity.ok().body(new Success<>(responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.ok().body(new Success<>());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePlan(@PathVariable Long id, @Valid @RequestBody UpdatePlanRequestDto requestDto, Errors errors) {
        if (errors.hasErrors()) {
            for (FieldError error : errors.getFieldErrors()) {
                throw new DefaultException(HttpStatus.BAD_REQUEST, error.getDefaultMessage());
            }
        }
        planService.updatePlan(id, requestDto);
        return ResponseEntity.ok().body(new Success<>());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> showDetail(@PathVariable Long id) {
        ShowDetailResponseDto responseDto = planService.showDetail(id);
        return ResponseEntity.ok().body(new Success<>(responseDto));
    }

    @GetMapping("/main")
    public ResponseEntity<Object> showMain() {
        List<ShowMainResponseDto> dtoList = planService.showMain();
        return ResponseEntity.ok().body(new Success<>(dtoList));
    }
}
