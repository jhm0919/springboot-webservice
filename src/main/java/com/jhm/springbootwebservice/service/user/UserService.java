package com.jhm.springbootwebservice.service.user;

import com.jhm.springbootwebservice.config.auth.dto.FindPasswordRequestDto;
import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.util.RedisUtil;
import com.jhm.springbootwebservice.config.auth.dto.FindUsernameRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void join(UserRequestDto dto) { // 사용자 비밀번호 해쉬 암호화 후 repository에 저장

        String email = dto.getEmail();
        String requestCode = dto.getCode();
        String storageCode = redisUtil.getData(email);

        if (requestCode.equals(storageCode)) {
            dto.setPassword(encoder.encode(dto.getPassword()));
            userRepository.save(dto.toEntity());
            redisUtil.deleteData(email);
        }
    }

    @Transactional
    public String findUsername(FindUsernameRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));
        String username = user.getUsername();
        return username;
    }

    @Transactional
    public void findPassword(FindPasswordRequestDto dto) throws MessagingException {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));
        String username = dto.getUsername();
        String email = dto.getEmail();

        if (user.getUsername().equals(username)) {
            String password = createCode();

            MimeMessage message = javaMailSender.createMimeMessage();

            message.setFrom(email);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "임시 비밀번호가 발급되었습니다." + "</h3>";
            body += "<h1>" + "임시 비밀번호 : " + password + "</h1>";
            body += "<h1>" + "로그인 후 비밀번호 변경을 해주세요." + "</h1>";
            body += "<a href='http://localhost:8080/auth/login'"+
            ">로그인 페이지</a>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");

            javaMailSender.send(message);

            user.update(encoder.encode(password));
        }
    }

    public String createCode() {
        int number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
        return "" + number;
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
                .body("이미 사용중인 닉네임 입니다.");
        }
    }
}
