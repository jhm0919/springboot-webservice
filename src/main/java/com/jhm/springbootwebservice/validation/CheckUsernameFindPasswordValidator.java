package com.jhm.springbootwebservice.validation;

import com.jhm.springbootwebservice.config.auth.dto.FindPasswordRequestDto;
import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckUsernameFindPasswordValidator extends AbstractValidator<FindPasswordRequestDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(FindPasswordRequestDto dto, Errors errors) {
        if (!userRepository.existsByUsername(dto.toEntity().getUsername())) {
            errors.rejectValue("username", "아이디 없음", "존재하지 않는 아이디입니다.");
        }
    }
}
