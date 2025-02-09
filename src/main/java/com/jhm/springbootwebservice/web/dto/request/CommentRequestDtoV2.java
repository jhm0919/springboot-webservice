package com.jhm.springbootwebservice.web.dto.request;

import com.jhm.springbootwebservice.domain.comment.Comment;
import com.jhm.springbootwebservice.domain.comment.CommentV2;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDtoV2 {

    private Long id;
    private Long parentId;
    private String comment;
    private User user;
    private Post post;

    /** DTO -> Entity */
    public CommentV2 toEntity() {
        return CommentV2.builder()
            .id(id)
            .comment(comment)
            .user(user)
            .post(post)
            .build();
    }
}
