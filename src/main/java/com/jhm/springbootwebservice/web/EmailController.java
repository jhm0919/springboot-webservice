package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.service.email.EmailService;
import com.jhm.springbootwebservice.web.dto.request.ConfirmRequestDto;
import jakarta.mail.MessagingException;
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
    public ResponseEntity<String> mailSend(@RequestBody String email) throws MessagingException {

        ResponseEntity<String> responseEntity = emailService.sendMail(email);
        log.info("응답={}", responseEntity);
        return responseEntity;
    }

    @PostMapping("/confirmCode")
    public ResponseEntity<String> confirmCode(@RequestBody ConfirmRequestDto dto) {
        return emailService.confirmCode(dto);
    }

    @PostMapping("/findUsernameMailSend")
    public ResponseEntity<String> findUsernameSendMail(@RequestBody String email) throws MessagingException {

        ResponseEntity<String> responseEntity = emailService.findUsernameSendMail(email);
        return responseEntity;
    }
}
