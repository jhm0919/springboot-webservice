package com.jhm.springbootwebservice.domain.comments.recommend;

import com.jhm.springbootwebservice.domain.posts.recommend.PostsRecommendPK;
import com.jhm.springbootwebservice.domain.posts.recommend.PostsRecommendUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRecommendUpRepository extends JpaRepository<CommentsRecommendUp, CommentsRecommendPK> {
    List<CommentsRecommendUp> findByPostsId(Long id);
}
