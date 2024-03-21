package com.jhm.springbootwebservice.validation;

import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckPasswordEqualValidator extends AbstractValidator<UserRequestDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserRequestDto dto, Errors errors) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "비밀번호 확인 오류", "비밀번호가 일치하지 않습니다.");
        }
    }
}
