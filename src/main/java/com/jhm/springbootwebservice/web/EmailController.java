package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestBody String email) {
        HashMap<String, Object> map = new HashMap<>();
//        try {
            // 존재하는 이메일인지 검증
//            boolean result = emailService.existsByEmail(email);
//            if (!result) {
            number = emailService.sendMail(email);
//            String num = String.valueOf(number);

        return ResponseEntity.ok("성공");
//        } catch (Exception e) {
//            return ResponseEntity.
//            map.put("success", Boolean.FALSE);
//            map.put("error", e.getMessage());
//        }

//        return map;
    }

    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(number));

        return ResponseEntity.ok(isMatch);
    }
}
