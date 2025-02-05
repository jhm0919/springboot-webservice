package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.recommend.RecommendService;
import com.jhm.springbootwebservice.web.dto.request.RecommendDto;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class RecommendApiController {

    private final RecommendService recommendService;

    @PutMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestBody RecommendRequestDto dto) {
        log.info("유저 아이디={}", dto.getUserId());

        return recommendService.recommend(dto);
//        return ResponseEntity.ok(Map.of("message", "Recommendation successful"));
//        RecommendRequestDto requestDto = new RecommendRequestDto(postId, commentId, user.getId(), recommendType);
//        return recommendService.recommend(requestDto);

    }
}
