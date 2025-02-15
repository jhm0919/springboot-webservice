package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.comment.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String author;
    private int recommendUp;
    private int recommendDown;
    private Long userId;
    private Long postId;
    private Long parentId;
    private Long groupNo;
    private int depthNo;
    private int level;
    private int isDeleted;
    private String createdDate;

    /** Entity -> DTO */
    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.comment = entity.getContent();
        this.author = entity.getUser().getName();
        this.recommendUp = entity.getRecommendUp();
        this.recommendDown = entity.getRecommendDown();
        this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
        this.userId = entity.getUser().getId();
        this.postId = entity.getPost().getId();
        this.groupNo = entity.getGroupNo();
        this.depthNo = entity.getDepthNo();
        this.level = entity.getLevel();
        this.isDeleted = entity.getIsDeleted();
        this.createdDate = entity.getCreatedDate();
    }
}
