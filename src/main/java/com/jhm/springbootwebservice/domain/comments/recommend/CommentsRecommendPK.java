package com.jhm.springbootwebservice.domain.comments.recommend;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Embeddable
public class CommentsRecommendPK implements Serializable {

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long commentId;
}
