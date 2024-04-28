package com.jhm.springbootwebservice.domain.posts;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.postimage.PostsImage;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {
    // Entity 클래스 에서는 절대 Setter 만들면 안됨
    // 대신 Builder 패턴으로 값을 채운 후 update 메소드를 호출하여 변경한다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String pureContent;

    private String author;

    @Column(columnDefinition = "integer default 0")
    private int view;

    @Column(columnDefinition = "integer default 0")
    private int commentSize;

    @Column(columnDefinition = "integer default 0")
    private int recommendUp;

    @Column(columnDefinition = "integer default 0")
    private int recommendDown;

    @ManyToOne(fetch = FetchType.LAZY) // @ManyToOne의 기본 Fetch 전략은 EAGER(즉시 로딩)이다.
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column
    private PostType postType;

    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;

    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostsImage> PostsImages;

    public void update(String title, String content, String pureContent, PostType postType) {
        this.title = title;
        this.content = content;
        this.pureContent = pureContent;
        this.postType = postType;
    }

    public void update(String content, String pureContent) {
        this.content = content;
        this.pureContent = pureContent;
    }


    public void commentSizeUp() {
        this.commentSize++;
    }

    public void commentSizeDown() {
        this.commentSize--;
    }

    public void increaseView() {
        this.view++;
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
}
