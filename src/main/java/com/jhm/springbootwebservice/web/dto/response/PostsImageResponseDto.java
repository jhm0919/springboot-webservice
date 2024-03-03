package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.postimage.PostsImage;
import lombok.Getter;

@Getter
public class PostsImageResponseDto {

    private Long id;
    private String name;
    private String url;
    private Long postId;

    public PostsImageResponseDto(PostsImage entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.url = entity.getUrl();
        this.postId = entity.getPosts().getId();
    }
}
