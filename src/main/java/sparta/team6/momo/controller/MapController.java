package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.service.MapService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {

    private final MapService mapService;

    @GetMapping("/{randomUrl}") // planId
    public ResponseEntity<?> getPlanId(@PathVariable("randomUrl") String url) {
        Long planId = mapService.getPlanId(url);
        return ResponseEntity.ok().body(new Success<>(planId));
    }
}
