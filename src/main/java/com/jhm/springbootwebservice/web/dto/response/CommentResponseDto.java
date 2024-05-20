package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.comments.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String author;
    private int recommendUp;
    private int recommendDown;
    private List<Comment> childrenComment;
    private String isParent;
    private Long userId;
    private Long postsId;
    private String createdDate;

    /** Entity -> DTO */
    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.comment = entity.getComment();
        this.author = entity.getUser().getName();
        this.recommendUp = entity.getRecommendUp();
        this.recommendDown = entity.getRecommendDown();
        this.childrenComment = entity.getChildrenComment();
        this.isParent = entity.getIsParent();
        this.userId = entity.getUser().getId();
        this.postsId = entity.getPosts().getId();
        this.createdDate = entity.getCreatedDate();
    }
}
