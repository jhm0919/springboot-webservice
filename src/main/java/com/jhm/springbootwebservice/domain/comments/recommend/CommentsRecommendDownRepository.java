package com.jhm.springbootwebservice.domain.comments.recommend;

import com.jhm.springbootwebservice.domain.posts.recommend.PostsRecommendDown;
import com.jhm.springbootwebservice.domain.posts.recommend.PostsRecommendPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRecommendDownRepository extends JpaRepository<CommentsRecommendDown, CommentsRecommendPK> {
    List<CommentsRecommendDown> findByPostsId(Long id);
}
