package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.MakePlanRequest;
import sparta.team6.momo.dto.MakePlanResponse;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.service.PlanService;

import javax.validation.Valid;

@RestController
@RequestMapping("/plans")
//@RequiredArgsConstructor
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

//    @PostMapping
//    public ResponseEntity<MakePlanResponse> makePlan(@Valid @RequestBody MakePlanRequest request) {
//        MakePlanResponse response = planService.savePlan(request);
//        return ResponseEntity.ok().body(new Success<>(response.getPostId()));
//    }


}
