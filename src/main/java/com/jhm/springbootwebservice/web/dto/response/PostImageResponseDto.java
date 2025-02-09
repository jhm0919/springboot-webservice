package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.postimage.PostImage;
import lombok.Getter;

@Getter
public class PostImageResponseDto {

    private Long id;
    private String name;
    private String url;
    private Long postId;

    public PostImageResponseDto(PostImage entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.url = entity.getUrl();
        this.postId = entity.getPost().getId();
    }
}
