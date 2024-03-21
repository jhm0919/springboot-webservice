package com.jhm.springbootwebservice.service.user;

import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long join(UserRequestDto dto) { // 사용자 비밀번호 해쉬 암호화 후 repository에 저장
        dto.setPassword(encoder.encode(dto.getPassword()));

        return userRepository.save(dto.toEntity()).getId();
    }

    // 회원가입 시 유효성 체크
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        HashMap<String, String> validatorResult = new HashMap<>();

        // 유효성 검사에 실패한 필드 목록을 받음
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

//    @Transactional(readOnly = true)
//    public void checkEmailDuplication(UserRequestDto dto) {
//        boolean emailDuplicate = userRepository.existsByEmail(dto.toEntity().getEmail());
//        if (emailDuplicate) {
//            throw new IllegalStateException("이미 존재하는 계정입니다.");
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public void checkNameDuplication(UserRequestDto dto) {
//        boolean nameDuplicate = userRepository.existsByName(dto.toEntity().getName());
//        if (nameDuplicate) {
//            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
//        }
//    }
}
