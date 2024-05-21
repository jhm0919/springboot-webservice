package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.comments.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class ReplyResponseDto {
    private Long id;
    private String comment;
    private String author;
    private int recommendUp;
    private int recommendDown;
//    private String isParent;
//    private String parentName;
    private Long userId;
    private Long postsId;
    private String createdDate;

    /** Entity -> DTO */
    public ReplyResponseDto(Comment entity) {
        this.id = entity.getId();
        this.comment = entity.getComment();
        this.author = entity.getUser().getName();
        this.recommendUp = entity.getRecommendUp();
        this.recommendDown = entity.getRecommendDown();
//        this.isParent = entity.getIsParent();
//        this.parentName = entity.getParentComment().getUser().getName();
        this.userId = entity.getUser().getId();
        this.postsId = entity.getPosts().getId();
        this.createdDate = entity.getCreatedDate();
    }
}
