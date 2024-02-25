package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.comments.CommentsService;
import com.jhm.springbootwebservice.web.dto.CommentRequestDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentsService commentsService;

    @PostMapping("/api/posts/{id}/comments")
    public Long commentSave(@PathVariable Long id,
                            @RequestBody CommentRequestDto commentRequestDto,
                            @LoginUser SessionUser user) {

        return commentsService.commentSave(user.getId(), id, commentRequestDto);
    }
}
