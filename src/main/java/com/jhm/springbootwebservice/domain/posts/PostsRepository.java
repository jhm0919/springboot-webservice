package com.jhm.springbootwebservice.domain.posts;

import com.jhm.springbootwebservice.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * SQL Mapper 에서의 Dao라고 불리는 DB Layer 접근자
 * Entity 클래스와 기본 Entity Repository는 함께 위치해야 함
 * Entity 클래스는 기본 Repository 없이는 제대로 역할을 할 수가 없음
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
//    Page<Posts> findAllDesc(User user, Pageable pageable);

//    Page<Posts> paging(User user, Pageable pageable);
}