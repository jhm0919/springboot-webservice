package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.comments.CommentsService;
import com.jhm.springbootwebservice.service.recommend.CommentsRecommendService;
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
    private final CommentsRecommendService commentsRecommendService;

    @PostMapping("/posts/{postId}/comments")
    public Long save(@PathVariable Long postId,
                     @RequestBody CommentRequestDto commentRequestDto,
                     @LoginUser SessionUser user) {

        return commentsService.save(user.getId(), postId, commentRequestDto);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public Long update(@PathVariable Long postId,
                       @PathVariable Long commentId,
                       @RequestBody CommentRequestDto dto) {

        return commentsService.update(postId, commentId, dto);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public Long delete(@PathVariable Long postId,
                       @PathVariable Long commentId) {
        commentsRecommendService.commentDelete(commentId); // 댓글에 있는 추천,비추천 삭제
        commentsService.delete(postId, commentId); // 댓글 삭제
        return commentId;
    }

    @PutMapping("/posts/{postId}/comments/{commentId}/recommend")
    public RecommendResponseDto recommend(@PathVariable Long postId,
                                          @PathVariable Long commentId,
                                          @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId(), commentId);
        RecommendResponseDto recommend = commentsRecommendService.recommend(requestDto);
        return recommend;
    }

    @PutMapping("/posts/{postId}/comments/{commentId}/disRecommend")
    public RecommendResponseDto disRecommend(@PathVariable Long postId,
                                             @PathVariable Long commentId,
                                             @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId(), commentId);
        RecommendResponseDto recommend = commentsRecommendService.disRecommend(requestDto);
        return recommend;
    }

}
