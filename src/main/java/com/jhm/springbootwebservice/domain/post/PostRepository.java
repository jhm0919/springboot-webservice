package com.jhm.springbootwebservice.domain.post;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * SQL Mapper 에서의 Dao라고 불리는 DB Layer 접근자
 * Entity 클래스와 기본 Entity Repository는 함께 위치해야 함
 * Entity 클래스는 기본 Repository 없이는 제대로 역할을 할 수가 없음
 */
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    List<Post> findAllByUserId(Long userId);

}