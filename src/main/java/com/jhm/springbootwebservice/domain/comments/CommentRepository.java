package com.jhm.springbootwebservice.domain.comments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByPostsIdAndId(Long postsId, Long id);
}
