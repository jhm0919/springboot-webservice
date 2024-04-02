package com.jhm.springbootwebservice.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    // 스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 앞에 있어야만 함
    USER("ROLE_USER", "일반 사용자"),
    OAUTH_USER("ROLE_OAUTH_USER", "소셜 사용자"),
    ADMIN("ROLE_ADMIN", "운영진");

    private final String key;
    private final String title;
}
