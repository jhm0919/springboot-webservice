package com.jhm.springbootwebservice.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * SQL Mapper 에서의 Dao라고 불리는 DB Layer 접근자
 * Entity 클래스와 기본 Entity Repository는 함께 위치해야 함
 * Entity 클래스는 기본 Repository 없이는 제대로 역할을 할 수가 없음
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {



    // postType이 없을 때
    // 제목+내용 검색조건
    Page<Posts> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    // 제목 검색조건
    Page<Posts> findByTitleContaining(String searchKeyword, Pageable pageable);
    // 내용 검색조건
    Page<Posts> findByContentContaining(String searchKeyword, Pageable pageable);
    // 작성자 검색조건
    Page<Posts> findByAuthorContaining(String searchKeyword, Pageable pageable);

    // postType이 선택되었을 때
    // postType + 전체조회
    Page<Posts> findAllByPostType(PostType postType, Pageable pageable);
    // postType + 제목+내용 검색조건
    Page<Posts> findByPostTypeAndTitleContainingOrPostTypeAndContentContaining(PostType type1, String title, PostType type2, String content, Pageable pageable);
    // postType + 제목 검색조건
    Page<Posts> findByPostTypeAndTitleContaining(PostType type, String searchKeyword, Pageable pageable);
    // postType + 내용 검색조건
    Page<Posts> findByPostTypeAndContentContaining(PostType type, String searchKeyword, Pageable pageable);
    // postType + 작성자 검색조건
    Page<Posts> findByPostTypeAndAuthorContaining(PostType type, String searchKeyword, Pageable pageable);
}