package com.jhm.springbootwebservice.domain.recommend;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
public class Recommend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

//    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Posts posts;

//    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    private Long recommendType;

}
