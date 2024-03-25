package com.jhm.springbootwebservice.domain.posts.recommend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRecommendDownRepository extends JpaRepository<PostsRecommendDown, PostsRecommendPK> {
    List<PostsRecommendDown> findByPostsId(Long id);
}
