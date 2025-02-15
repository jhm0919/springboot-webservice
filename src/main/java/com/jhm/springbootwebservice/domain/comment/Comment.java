package com.jhm.springbootwebservice.domain.comment;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.recommend.CommentRecommend;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0")
    private int recommendUp;

    @Column(columnDefinition = "integer default 0")
    private int recommendDown;

    @ManyToOne(fetch = FetchType.LAZY) // 부모 댓글 (대댓글인 경우)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(columnDefinition = "integer default 0")
    private int isDeleted;

    private Long groupNo;

    @Column(columnDefinition = "integer default 0")
    private int depthNo;

    @Column(columnDefinition = "integer default 0")
    private int level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommentRecommend> commentRecommends;

    public void update(String content) {
        this.content = content;
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

    public void updatePost(Post post) {
        this.post = post;
    }

    public void updateIsDeleted() {
        this.isDeleted = 1;
    }

    // **groupNo를 업데이트하는 메서드**
    public void updateGroupNo(Long groupNo) {
        if (this.groupNo == null) { // 한 번만 설정되도록
            this.groupNo = groupNo;
        }
    }
}
