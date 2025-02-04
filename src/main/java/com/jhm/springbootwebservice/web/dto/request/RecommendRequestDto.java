package com.jhm.springbootwebservice.web.dto.request;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendRequestDto {

    private Long postId;
    private Long userId;
    private Long commentId;
    private Long recommendType;

    public RecommendRequestDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
