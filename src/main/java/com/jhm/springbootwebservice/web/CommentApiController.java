package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.comments.CommentsService;
import com.jhm.springbootwebservice.service.recommend.RecommendService;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentsService commentsService;
    private final RecommendService recommendService;

    @PostMapping("/posts/{id}/comments")
    public Long save(@PathVariable Long id,
                     @RequestBody CommentRequestDto commentRequestDto,
                     @LoginUser SessionUser user) {

        return commentsService.save(user.getId(), id, commentRequestDto);
    }

    @PutMapping("/posts/{postsId}/comments/{id}")
    public Long update(@PathVariable Long postsId,
                       @PathVariable Long id,
                       @RequestBody CommentRequestDto dto) {

        return commentsService.update(postsId, id, dto);
    }

    @DeleteMapping("/posts/{postsId}/comments/{id}")
    public Long delete(@PathVariable Long postsId,
                       @PathVariable Long id) {

        return commentsService.delete(postsId, id);
    }

    @PutMapping("/posts/{postId}/comments/recommend")
    public RecommendResponseDto recommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = recommendService.recommend(requestDto);
        int recommendUpCount = recommend.getRecommendUpCount();
        int recommendDownCount = recommend.getRecommendDownCount();
        boolean isRecommend = recommendService.findById(requestDto).isRecommend();

        RecommendResponseDto recommendResponseDto =
            new RecommendResponseDto(isRecommend, recommendUpCount, recommendDownCount);
        return recommendResponseDto;
    }

    @PutMapping("/posts/{postId}/comments/disRecommend")
    public RecommendResponseDto disRecommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = recommendService.disRecommend(requestDto);
        int recommendUpCount = recommend.getRecommendUpCount();
        int recommendDownCount = recommend.getRecommendDownCount();
        boolean isRecommend = recommendService.findById(requestDto).isRecommend();

        RecommendResponseDto recommendResponseDto =
            new RecommendResponseDto(isRecommend, recommendUpCount, recommendDownCount);
        return recommendResponseDto;
    }

}
