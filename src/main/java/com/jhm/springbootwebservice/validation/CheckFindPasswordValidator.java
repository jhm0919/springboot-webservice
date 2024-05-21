package com.jhm.springbootwebservice.validation;

import com.jhm.springbootwebservice.config.auth.dto.FindPasswordRequestDto;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Slf4j
@RequiredArgsConstructor
@Component
public class CheckFindPasswordValidator extends AbstractValidator<FindPasswordRequestDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(FindPasswordRequestDto dto, Errors errors) {
        boolean result = userRepository.existsByUsername(dto.toEntity().getUsername());
        log.info("result={}", result);
        if (result) {
            User user = userRepository.findByUsername(dto.toEntity().getUsername()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));
            String email = user.getEmail();
            if (!dto.getEmail().equals(email)) {
                errors.rejectValue("email", "일치하지 않는 이메일", "입력하신 계정으로 가입된 이메일이 아닙니다.");
            }
        } else {
            errors.rejectValue("username", "일치하지 않는 이메일", "존재하지 않는 아이디입니다.");
        }

    }
}
