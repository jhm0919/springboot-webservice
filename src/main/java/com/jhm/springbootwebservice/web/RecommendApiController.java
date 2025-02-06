package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.service.recommend.RecommendService;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class RecommendApiController {

    private final RecommendService recommendService;

    @PutMapping("/recommend")
    public ResponseEntity<String> recommend(@RequestBody RecommendRequestDto dto) {

        ResponseEntity<String> result = recommendService.recommend(dto);
        log.info("메시지={}", result);
        return result;
    }
}
