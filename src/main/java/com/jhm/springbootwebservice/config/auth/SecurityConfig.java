package com.jhm.springbootwebservice.config.auth;

import com.jhm.springbootwebservice.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
/**
 * @EnableWebSecurity
 * Spring Security 설정들을 활성화 시켜줌
 */
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) /** h2-console 화면을 사용하기 위해 해당 옵션들을 disable
                                                                                                (개발 환경에서만)*/
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth /** URL별 권한 관리를 설정하는 옵션 */
                        .requestMatchers("/", "/scss/**", "/css/**", "/files/**", "/img/**", "/js/**", "/posts/read/**", "/auth/login"
                        ).permitAll()
                        /** 권한 관리 대상을 지정하는 옵션
                         * URL, HTTP 메소드별로 관리가 가능
                         * "/" 등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한을 줌 */
                        .requestMatchers("/api/**").hasRole(Role.USER.name())
                        /** "/api/**" 주소를 가진 API는 USER 권한을 가진 사람만 가능 */
                        .anyRequest().authenticated()) /** 설정된 값들 이외 나머지 URL들을 나타냄
                                                         여기서는 authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용
                                                         인증된 사용자 즉, 로그인한 사용자들을 이야기함 */
                .logout(logout -> logout
                        .logoutSuccessUrl("/")) /** 로그아웃 기능에 대한 여러 설정의 진입점. 로그아웃 성공시 /주소로 이동 */
                .oauth2Login(oauth2Login -> oauth2Login /** OAuth2 로그인 기능에 대한 여러 설정의 진입점 */
                        .userInfoEndpoint(userinfoEndpoint -> userinfoEndpoint /** OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당 */
                                .userService(customOAuth2UserService)));
//                        .defaultSuccessUrl("/", true));
                                                                        /** 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록함
                                                                         리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자
                                                                         하는 기능을 명시
                                                                         */
        return http.build();
    }
}
