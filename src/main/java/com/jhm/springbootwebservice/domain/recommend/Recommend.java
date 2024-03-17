package com.jhm.springbootwebservice.domain.recommend;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recommend extends BaseTimeEntity {

    @EmbeddedId
    private RecommendPK id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Posts posts;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
