package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PutMapping("/update")
    public Long modify(@RequestBody UserRequestDto dto) {
        userService.modify(dto);

        // 변경된 세션 등록
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return dto.getId();
    }
}
