package com.jhm.springbootwebservice.web.dto.response;

import com.jhm.springbootwebservice.domain.comment.CommentV2;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.post.PostType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private int view;
    private int commentSize;
    private PostType postType;
    private int recommendUp;
    private int recommendDown;
    private String createdDate;
    private String modifiedDate;
    private Long userId;
//    private List<CommentResponseDto> comments;
//    private List<CommentResponseDtoV2> comments;
    private List<CommentResponseDtoV2> comments;
    private List<PostImageResponseDto> postImages;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.view = entity.getView();
        this.commentSize = entity.getCommentSize();
        this.postType = entity.getPostType();
        this.recommendUp = entity.getRecommendUp();
        this.recommendDown = entity.getRecommendDown();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.userId = entity.getUser().getId();
//        this.comments = entity.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.comments = entity.getComments().stream().map(CommentResponseDtoV2::new).collect(Collectors.toList());
//        this.comments = entity.getComments();
//        this.imageUrls = entity.getPostsImages().stream().map(PostsImage::getUrl).collect(Collectors.toList());
        this.postImages = entity.getPostImages().stream().map(PostImageResponseDto::new).collect(Collectors.toList());
    }
}
