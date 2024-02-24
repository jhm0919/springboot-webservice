package com.jhm.springbootwebservice.web.dto;

import com.jhm.springbootwebservice.domain.comments.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String author;
    private LocalDateTime modifiedDate;
    private Long postId;

    /** Entity -> DTO */
    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.comment = entity.getComment();
        this.author = entity.getUser().getName();
        this.postId = entity.getPosts().getId();
        this.modifiedDate = entity.getModifiedDate();
    }
}
