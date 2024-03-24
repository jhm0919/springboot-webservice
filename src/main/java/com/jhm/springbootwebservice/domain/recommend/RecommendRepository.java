package com.jhm.springbootwebservice.domain.recommend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend, RecommendPK> {
    Long deleteByPostsId(Long id);

    List<Recommend> findByPostsId(Long id);
}
