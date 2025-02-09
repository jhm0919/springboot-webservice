package com.jhm.springbootwebservice.domain.comment;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommentV2 extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(columnDefinition = "integer default 0")
    private int recommendUp;

    @Column(columnDefinition = "integer default 0")
    private int recommendDown;

    @ManyToOne(fetch = FetchType.LAZY) // 부모 댓글 (대댓글인 경우)
    @JoinColumn(name = "parent_id")
    private CommentV2 parent;

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

    public void updatePost(Post post) {
        this.post = post;
    }

    // **groupNo를 업데이트하는 메서드**
    public void updateGroupNo(Long groupNo) {
        if (this.groupNo == null) { // 한 번만 설정되도록
            this.groupNo = groupNo;
        }
    }
}
