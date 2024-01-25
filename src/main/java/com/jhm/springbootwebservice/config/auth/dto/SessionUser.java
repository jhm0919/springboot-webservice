package com.jhm.springbootwebservice.config.auth.dto;

import com.jhm.springbootwebservice.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * 인증된 사용자 정보만 필요
 * 직렬화 기능을 가진 세션 Dto
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
