package com.jhm.springbootwebservice.web.dto.request;

import lombok.Getter;

@Getter
public class RecommendDto {
    private Long postId;      // 게시물 추천 시 사용
    private Long commentId;   // 댓글 추천 시 사용
    private Long userId;
    private int recommendType;
}
