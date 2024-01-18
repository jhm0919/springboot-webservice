package com.jhm.springbootwebservice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Entity 클래스와 기본 Entity Repository는 함께 위치해야 함
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {// SQL Mapper 에서의 Dao라고 불리는 DB Layer 접근자

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
