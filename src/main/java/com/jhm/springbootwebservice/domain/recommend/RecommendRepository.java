package com.jhm.springbootwebservice.domain.recommend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    // 게시글 추천 중복 확인 (댓글이 null인 경우)
    boolean existsByUserIdAndPostsIdAndCommentIdIsNull(Long userId, Long postsId);

    // 댓글 추천 중복 확인 (comment가 null이 아닌 경우)
    boolean existsByUserIdAndPostsIdAndCommentId(Long userId, Long postsId, Long commentId);

    // 게시글 추천, 비추천, 댓글 추천, 비추천 조회 후 카운트 리턴

}
