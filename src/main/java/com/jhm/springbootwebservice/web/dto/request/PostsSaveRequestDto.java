package com.jhm.springbootwebservice.web.dto.request;

import com.jhm.springbootwebservice.domain.posts.PostType;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.user.User;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;
    private PostType postType;
    private int view;
    private User user;

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .postType(postType)
                .view(0)
                .user(user)
                .build();
    }
}
