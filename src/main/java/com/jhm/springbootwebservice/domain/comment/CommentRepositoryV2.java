package com.jhm.springbootwebservice.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepositoryV2 extends JpaRepository<CommentV2, Long> {

    //    Comment findByPostIdAndId(Long postsId, Long id);

    List<CommentV2> findByGroupNoAndDepthNo(Long groupNo, int depthNo);
}
