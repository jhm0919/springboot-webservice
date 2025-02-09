package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.comment.Comment;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String author;
    private int recommendUp;
    private int recommendDown;
    private List<ReplyResponseDto> childrenComments;
    private String isParent;
    private Long userId;
    private Long postId;
    private String createdDate;

    /** Entity -> DTO */
    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.comment = entity.getComment();
        this.author = entity.getUser().getName();
        this.recommendUp = entity.getRecommendUp();
        this.recommendDown = entity.getRecommendDown();
        this.childrenComments = entity.getChildrenComment().stream().map(ReplyResponseDto::new).collect(Collectors.toList());
        this.isParent = entity.getIsParent();
        this.userId = entity.getUser().getId();
        this.postId = entity.getPost().getId();
        this.createdDate = entity.getCreatedDate();
    }
}
