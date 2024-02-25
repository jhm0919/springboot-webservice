package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.comments.CommentsService;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentsService commentsService;

    @PostMapping("/api/posts/{id}/comments")
    public Long save(@PathVariable Long id,
                     @RequestBody CommentRequestDto commentRequestDto,
                     @LoginUser SessionUser user) {

        return commentsService.save(user.getId(), id, commentRequestDto);
    }

    @PutMapping("/api/posts/{postsId}/comments/{id}")
    public Long update(@PathVariable Long postsId,
                       @PathVariable Long id,
                       @RequestBody CommentRequestDto dto) {

        return commentsService.update(postsId, id, dto);
    }

    @DeleteMapping("/api/posts/{postsId}/comments/{id}")
    public Long delete(@PathVariable Long postsId,
                       @PathVariable Long id) {

        return commentsService.delete(postsId, id);
    }

}
