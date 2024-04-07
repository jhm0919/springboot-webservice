package com.jhm.springbootwebservice.service.user;

import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 회원 수정 (Dirty checking)
    @Transactional
    public ResponseEntity<String> modify(UserRequestDto dto) {
        User user = userRepository.findById(dto.toEntity().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 회원이 존재하지 않습니다."));

        String password = encoder.encode(dto.getPassword()); // 비밀번호 인코딩
        boolean result = userRepository.existsByName(dto.getName()); // 닉네임이 이미 있으면 true
        if (!result) {
            user.update(dto.getName(), password);
            return ResponseEntity.ok("성공");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("이름이 이미 존재합니다.");
        }
    }
}
