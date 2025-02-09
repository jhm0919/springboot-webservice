package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.post.Post;
import lombok.Getter;


@Getter
public class PostListResponseDto {
    private Long id;
    private String title;
    private String author;
    private String postType;
    private int commentsSize;
    private int view;
    private int recommendUp;
    private String modifiedDate;

    public PostListResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.postType = entity.getPostType().getTitle();
        this.commentsSize = entity.getComments().size();
        this.view = entity.getView();
        this.recommendUp = entity.getRecommendUp();
        this.modifiedDate = entity.getModifiedDate();

    }
}
