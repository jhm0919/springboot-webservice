package com.jhm.springbootwebservice.domain.posts.recommend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRecommendUpRepository extends JpaRepository<PostsRecommendUp, PostsRecommendPK> {
    List<PostsRecommendUp> findByPostsId(Long id);
}
