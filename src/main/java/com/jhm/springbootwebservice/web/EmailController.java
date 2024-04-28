package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.service.email.EmailService;
import com.jhm.springbootwebservice.web.dto.request.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestBody String email) {
        boolean result = emailService.existsByEmail(email);
        ResponseEntity<String> responseEntity = null;
        if (!result) {
            responseEntity = emailService.sendMail(email);
        }
        return responseEntity;
    }

    @PostMapping("/mailCheck")
    public ResponseEntity<Boolean> mailCheck(@RequestBody EmailDto dto) {
        return ResponseEntity.ok(emailService.confirmEmail(dto));
    }
}
