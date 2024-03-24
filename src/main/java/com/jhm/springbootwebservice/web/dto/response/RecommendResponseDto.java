package com.jhm.springbootwebservice.web.dto.response;

import lombok.Getter;

@Getter
public class RecommendResponseDto {
    private boolean isRecommend;
    private int recommendUpCount;
    private int recommendDownCount;

    public RecommendResponseDto(boolean isRecommend, int recommendUpCount, int recommendDownCount) {
        this.isRecommend = isRecommend;
        this.recommendUpCount = recommendUpCount;
        this.recommendDownCount = recommendDownCount;
    }

}
