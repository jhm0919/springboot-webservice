package com.jhm.springbootwebservice.domain.recommend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class RecommendPK implements Serializable {

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;
}
