package com.jhm.springbootwebservice.web.dto.request;

import com.jhm.springbootwebservice.domain.post.PostType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private PostType postType;
    private String title;
    private String content;

    @Builder
    public PostUpdateRequestDto(String title, String content, PostType postType) {
        this.postType = postType;
        this.title = title;
        this.content = content;
    }
}
