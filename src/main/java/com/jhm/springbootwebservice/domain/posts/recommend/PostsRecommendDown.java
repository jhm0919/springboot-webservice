package com.jhm.springbootwebservice.domain.posts.recommend;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PostsRecommendDown extends BaseTimeEntity {

    @EmbeddedId
    private PostsRecommendPK id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Posts posts;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
