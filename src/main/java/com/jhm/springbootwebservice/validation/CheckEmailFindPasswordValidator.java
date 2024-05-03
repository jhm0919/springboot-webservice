package com.jhm.springbootwebservice.validation;

import com.jhm.springbootwebservice.config.auth.dto.FindPasswordRequestDto;
import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckEmailFindPasswordValidator extends AbstractValidator<FindPasswordRequestDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(FindPasswordRequestDto dto, Errors errors) {
        if (!userRepository.existsByEmail(dto.toEntity().getEmail())) {
            errors.rejectValue("email", "없는 이메일", "가입된 계정의 이메일이 아닙니다.");
        }
    }
}
