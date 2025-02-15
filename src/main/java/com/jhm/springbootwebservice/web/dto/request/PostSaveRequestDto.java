package com.jhm.springbootwebservice.web.dto.request;

import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.post.PostType;
import com.jhm.springbootwebservice.domain.user.User;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveRequestDto {
    private String title;
    private String content;
    private String pureContent;
    private String author;
    private PostType postType;
    private int view;
    private User user;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .pureContent(pureContent)
                .author(author)
                .postType(postType)
                .view(0)
                .user(user)
                .build();
    }
}
