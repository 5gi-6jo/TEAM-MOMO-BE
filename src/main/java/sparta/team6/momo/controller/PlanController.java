package sparta.team6.momo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.*;
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


//    @Operation(summary = "test hello", description = "hello api example")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK !!"),
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
//            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
//            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
//    })

    @PostMapping
    public ResponseEntity<Object> makePlan(@Valid @RequestBody MakePlanRequestDto requestDto) {
        MakePlanResponseDto responseDto = planService.savePlan(requestDto);
        return ResponseEntity.ok().body(new Success<>(responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.ok().body(new Success<>());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePlan(@PathVariable Long id, @RequestBody UpdatePlanRequestDto requestDto) {
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
