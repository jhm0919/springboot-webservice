package com.jhm.springbootwebservice.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


//    Optional<User> findByUsername(String username);

    // 소셜 로그인으로 반환되는 값 중 email을 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지 판단하기 위한 메서드
    Optional<User> findByEmail(String email);

//    User findByProviderAndProviderId(String provider, String providerId);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

//    User findByName(String name);
}
