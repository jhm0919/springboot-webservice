package com.jhm.springbootwebservice.web.dto.request;

import com.jhm.springbootwebservice.domain.comment.Comment;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.user.User;
import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private Long id;
    private Long hasParent;
    private Long parentId;
    private String comment;
    private Long groupNo;
    private int depthNo;
    private int level;
    private User user;
    private Post post;

    /** DTO -> Entity */
    public Comment toEntity() {
        return Comment.builder()
            .id(id)
            .comment(comment)
            .user(user)
            .post(post)
            .build();
    }
}
