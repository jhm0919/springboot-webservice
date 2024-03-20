package com.jhm.springbootwebservice.config.auth.dto;

import com.jhm.springbootwebservice.domain.user.Role;
import com.jhm.springbootwebservice.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String name;
    private String email;
    private String password;
    private String picture;
    private Role role;

    /* DTO -> Entity */
    public User toEntity() {
        return User.builder()
            .name(name)
            .email(email)
            .password(password)
            .picture(picture)
            .role(Role.USER)
            .build();
    }
}
