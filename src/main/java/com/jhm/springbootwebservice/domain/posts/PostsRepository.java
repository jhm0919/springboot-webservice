package com.jhm.springbootwebservice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SQL Mapper 에서의 Dao라고 불리는 DB Layer 접근자
 * Entity 클래스와 기본 Entity Repository는 함께 위치해야 함
 * Entity 클래스는 기본 Repository 없이는 제대로 역할을 할 수가 없음
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {
}