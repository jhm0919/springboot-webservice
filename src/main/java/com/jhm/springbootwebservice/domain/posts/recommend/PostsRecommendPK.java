package com.jhm.springbootwebservice.domain.posts.recommend;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Embeddable
public class PostsRecommendPK implements Serializable {

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;
}
