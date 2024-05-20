package com.jhm.springbootwebservice.domain.comments;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(columnDefinition = "integer default 0")
    private int recommendUp;

    @Column(columnDefinition = "integer default 0")
    private int recommendDown;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment; //부모 댓글

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> childrenComment = new ArrayList<>(); //자식 댓글들(대댓글)

    private String isParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(String comment) {
        this.comment = comment;
    }

    public void recommendUp() {
        this.recommendUp++;
    }

    public void recommendDown() {
        this.recommendUp--;
    }

    public void disRecommendUp(){
        this.recommendDown++;
    }

    public void disRecommendDown() {
        this.recommendDown--;
    }

    public void updateAuthor(User user) {
        this.user = user;
    }

    public void updatePosts(Posts post) {
        this.posts = post;
    }

    public void updateParent(Comment comment) {
        this.parentComment = comment;
    }
}
