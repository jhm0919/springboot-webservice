package com.jhm.springbootwebservice.domain.postimage;

import com.jhm.springbootwebservice.domain.posts.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PostsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;

    public PostsImage(PostsImage postsImage) {

    }
}
