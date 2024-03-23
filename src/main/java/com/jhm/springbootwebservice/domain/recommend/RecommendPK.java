package com.jhm.springbootwebservice.domain.recommend;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Embeddable
public class RecommendPK implements Serializable {

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;
}
