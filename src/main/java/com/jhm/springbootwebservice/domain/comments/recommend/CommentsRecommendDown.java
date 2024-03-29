package com.jhm.springbootwebservice.domain.comments.recommend;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.recommend.PostsRecommendPK;
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
public class CommentsRecommendDown extends BaseTimeEntity {

    @EmbeddedId
    private CommentsRecommendPK id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Posts posts;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
}
