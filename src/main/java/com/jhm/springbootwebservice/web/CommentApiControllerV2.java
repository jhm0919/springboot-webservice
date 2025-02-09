package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.comment.CommentService;
import com.jhm.springbootwebservice.service.comment.CommentServiceImplV2;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDtoV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentApiControllerV2 {

    private final CommentServiceImplV2 commentService;

    @PostMapping("/post/{postId}/comment")
    public Long save(@PathVariable Long postId,
                     @RequestBody CommentRequestDtoV2 commentRequestDto,
                     @LoginUser SessionUser user) {

        return commentService.save(user.getId(), postId, commentRequestDto);
    }

    @PutMapping("/post/{postId}/comment/{commentId}")
    public Long update(@PathVariable Long postId,
                       @PathVariable Long commentId,
                       @RequestBody CommentRequestDto dto) {

        return commentService.update(postId, commentId, dto);
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public Long delete(@PathVariable Long postId,
                       @PathVariable Long commentId) {
        commentService.delete(postId, commentId); // 댓글 삭제
        return commentId;
    }

}
