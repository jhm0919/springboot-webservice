package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private int view;
    private String modifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.view = entity.getView();
        this.modifiedDate = entity.getModifiedDate();

    }
}