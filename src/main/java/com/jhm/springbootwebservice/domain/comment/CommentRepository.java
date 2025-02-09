package com.jhm.springbootwebservice.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByPostIdAndId(Long postsId, Long id);
}
