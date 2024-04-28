package com.jhm.springbootwebservice.domain.validation;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmailValidation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String code;

    private Boolean confirm;

    public void update(Boolean confirm) {
        this.confirm = confirm;
    }
}
