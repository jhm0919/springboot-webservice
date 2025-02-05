package com.jhm.springbootwebservice.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendResponseDtoV2 {
    private int postRecommendUpCount;
    private int postRecommendDownCount;
    private int commentRecommendUpCount;
    private int commentRecommendDownCount;

}
