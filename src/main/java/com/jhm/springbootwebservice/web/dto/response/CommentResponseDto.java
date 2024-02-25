package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.comments.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String author;
    private String createdDate;
    private Long userId;
    private Long postsId;

    /** Entity -> DTO */
    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.comment = entity.getComment();
        this.author = entity.getUser().getName();
        this.userId = entity.getUser().getId();
        this.postsId = entity.getPosts().getId();
        this.createdDate = entity.getCreatedDate();
    }
}
