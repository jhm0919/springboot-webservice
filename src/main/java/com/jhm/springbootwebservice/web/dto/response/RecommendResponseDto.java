package com.jhm.springbootwebservice.web.dto.response;

import lombok.Getter;

@Getter
public class RecommendResponseDto {
    private boolean isRecommend;
    private int recommendCount;

    public RecommendResponseDto(boolean isRecommend, int recommendCount) {
        this.isRecommend = isRecommend;
        this.recommendCount = recommendCount;
    }
}
