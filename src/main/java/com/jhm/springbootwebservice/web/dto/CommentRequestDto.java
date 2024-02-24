package com.jhm.springbootwebservice.web.dto;

import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private Long id;
    private String comment;
    private User user;
    private Posts posts;

    /** DTO -> Entity */
    public Comment toEntity() {
        return Comment.builder()
            .id(id)
            .comment(comment)
            .user(user)
            .posts(posts)
            .build();
    }
}
