package com.jhm.springbootwebservice.domain.recommend;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.comment.Comment;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_comment_recommend", columnNames = {"comment_id", "user_id", "post_id"})
        }
)
public class CommentRecommend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // 게시글 추천이면 값이 있음
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id") // 댓글 추천이면 값이 있음
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int recommendType;

    @Builder // 빌더 패턴으로만 객체 생성하도록 유도
    public CommentRecommend(Post post, Comment comment, User user, int recommendType) {
        this.post = post;
        this.comment = comment;
        this.user = user;
        this.recommendType = recommendType;
    }

}

