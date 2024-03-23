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

}
