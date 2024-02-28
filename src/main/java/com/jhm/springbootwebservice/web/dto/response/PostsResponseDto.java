package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.postimage.PostsImage;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private int view;
    private Long userId;
    private List<CommentResponseDto> comments;
    private List<String> imageUrls;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.view = entity.getView();
        this.userId = entity.getUser().getId();
        this.comments = entity.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.imageUrls = entity.getPostsImages().stream().map(PostsImage::getUrl).collect(Collectors.toList());
    }
}
