package com.jhm.springbootwebservice.domain.user;

import com.jhm.springbootwebservice.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // 테이블 이름을 "users"로 변경
public class User extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 100)
    private String password;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User socialUpdate(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void update(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void update(String password) {
        this.password = password;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}

