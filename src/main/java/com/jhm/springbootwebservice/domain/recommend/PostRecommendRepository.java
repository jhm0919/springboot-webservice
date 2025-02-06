package com.jhm.springbootwebservice.domain.recommend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {

    // 게시글 추천 중복 확인 (댓글이 null인 경우)
    boolean existsByUserIdAndPostId(Long userId, Long postsId);

    PostRecommend findByUserIdAndPostId(Long userId, Long PostId);

}
