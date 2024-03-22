package com.jhm.springbootwebservice.config.auth.dto;

import com.jhm.springbootwebservice.domain.user.Role;
import com.jhm.springbootwebservice.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * 인증된 사용자 정보만 필요
 * 직렬화 기능을 가진 세션 Dto
 */
@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private Role role;
    private String modifiedDate;

    public SessionUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
        this.modifiedDate = user.getModifiedDate();
    }
}
