package com.jhm.springbootwebservice.config.auth;

import com.jhm.springbootwebservice.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
@Configuration
/**
 * @EnableWebSecurity
 * Spring Security 설정들을 활성화 시켜줌
 */
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationFailureHandler customFailureHandler;


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(auth -> auth.disable()) /* h2-console 화면을 사용하기 위해 해당 옵션들을 disable (개발 환경에서만)*/
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/scss/**", "/css/**", "/files/**", "/img/**", "/js/**", "/posts/read/**", "/auth/**", "/error/**")
                .permitAll()
                .requestMatchers("/api/**").hasRole(Role.USER.name())
                .anyRequest().authenticated())
            .formLogin(formLogin -> formLogin
                .loginPage("/auth/login")
                .usernameParameter("email")
                .loginProcessingUrl("/loginProc")
                .failureHandler(customFailureHandler)
                .defaultSuccessUrl("/", true))
            .logout(logout -> logout
                .logoutSuccessUrl("/"))
            .oauth2Login(oauth2Login -> oauth2Login /* OAuth2 로그인 기능에 대한 여러 설정의 진입점 */
                .userInfoEndpoint(userinfoEndpoint -> userinfoEndpoint /* OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당 */
                .userService(customOAuth2UserService))
                .defaultSuccessUrl("/", true));//.userDetailsService(customUserDetailsService);

                 /* 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록함
                  리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자
                  하는 기능을 명시*/
        return http.build();
    }
}
