package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.posts.PostsService;
import com.jhm.springbootwebservice.service.recommend.CommentsRecommendService;
import com.jhm.springbootwebservice.service.recommend.PostsRecommendService;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsUpdateRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final PostsRecommendService postsRecommendService;
    private final CommentsRecommendService commentsRecommendService;

    @PostMapping(value = "/posts")
    public Long save(@RequestBody PostsSaveRequestDto postsSaveRequestDto,
                     @LoginUser SessionUser user) throws Exception {
        Long postId = postsService.save(user.getId(), postsSaveRequestDto);

        return postId;
    }

    @PutMapping(value = "/posts/{postId}")
    public Long update(@PathVariable Long postId,
                       @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(postId, requestDto);
    }

    @DeleteMapping("/posts/{postId}")
    public Long delete(@PathVariable Long postId) throws IOException {
        postsRecommendService.delete(postId); // 게시글 추천, 비추천 삭제
        commentsRecommendService.postDelete(postId); // 댓글에 있는 추천, 비추천 삭제
        postsService.delete(postId);
        return postId;
    }

    @PutMapping("/posts/{postId}/recommend")
    public RecommendResponseDto recommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = postsRecommendService.recommend(requestDto);
        return recommend;
    }

    @PutMapping("/posts/{postId}/disRecommend")
    public RecommendResponseDto disRecommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = postsRecommendService.disRecommend(requestDto);
        return recommend;
    }
}
