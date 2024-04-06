package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;

public interface RecommendService {

//    RecommendResponseDto findById(RecommendRequestDto requestDto);

    void delete(Long id);

    RecommendResponseDto recommend(RecommendRequestDto requestDto);

    RecommendResponseDto disRecommend(RecommendRequestDto requestDto);
}
