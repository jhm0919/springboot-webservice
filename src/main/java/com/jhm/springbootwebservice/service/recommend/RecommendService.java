package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;

public interface RecommendService {

    RecommendResponseDto recommend(Long postId, Long userId);

    Long cancel(Long id);
}
