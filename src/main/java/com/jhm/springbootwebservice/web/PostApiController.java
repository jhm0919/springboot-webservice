package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.post.PostService;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping(value = "/post")
    public Long save(@RequestBody PostsSaveRequestDto postsSaveRequestDto,
                     @LoginUser SessionUser user) throws Exception {
        Long postId = postService.save(user.getId(), postsSaveRequestDto);

        return postId;
    }

    @PutMapping(value = "/post/{postId}")
    public Long update(@PathVariable Long postId,
                       @RequestBody PostUpdateRequestDto requestDto) {
        return postService.update(postId, requestDto);
    }

    @DeleteMapping("/post/{postId}")
    public Long delete(@PathVariable Long postId) throws IOException {
        postService.delete(postId);
        return postId;
    }

//    @PutMapping("/posts/{postId}/recommend")
//    public RecommendResponseDto recommend(@PathVariable Long postId, @LoginUser SessionUser user) {
//        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());
//
//        ResponseEntity<String> recommend1 = recommendService.recommend(requestDto);
////        RecommendResponseDto recommend = postsRecommendService.recommend(requestDto);
//        return recommend;
//    }

//    @PutMapping("/posts/{postId}/disRecommend")
//    public RecommendResponseDto disRecommend(@PathVariable Long postId, @LoginUser SessionUser user) {
//        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());
//
//        RecommendResponseDto recommend = postsRecommendService.disRecommend(requestDto);
//        return recommend;
//    }
}
