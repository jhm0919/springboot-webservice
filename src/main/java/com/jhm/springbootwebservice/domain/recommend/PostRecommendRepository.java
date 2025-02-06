package com.jhm.springbootwebservice.domain.recommend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {

    // 게시글 추천 중복 확인 (댓글이 null인 경우)
    boolean existsByUserIdAndPostIdAnd(Long userId, Long postsId);

    // 댓글 추천 중복 확인 (comment가 null이 아닌 경우)
//    boolean existsByUserIdAndPostIdAndCommentId(Long userId, Long postId, Long commentId);

    PostRecommend findByUserIdAndPostId(Long userId, Long PostId);

//    PostRecommend findByUserIdAndPostIdAndCommentId(Long userId, Long PostId, Long commentId);

}
