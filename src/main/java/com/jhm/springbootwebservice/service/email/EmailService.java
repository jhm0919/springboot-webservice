package com.jhm.springbootwebservice.service.email;

import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public String createCode() {
        int number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
        return "" + number;
    }

    private MimeMessage createMail(String email) throws MessagingException {

        String authCode = createCode();
        log.info("인증코드={}", authCode);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(email);
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("이메일 인증");
        String body = "";
        body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
        body += "<h1>" + authCode + "</h1>";
        body += "<h3>" + "감사합니다." + "</h3>";
        message.setText(body,"UTF-8", "html");

        redisUtil.setDataExpire(email, authCode, 60 * 1L); // 1분동안 저장

        return message;
    }

    public ResponseEntity<String> sendMail(String email) throws MessagingException {
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("사용할 수 없는 이메일입니다.");
        }

        if (redisUtil.existData(email)) {
            redisUtil.deleteData(email);
        }

        MimeMessage message = createMail(email);

        javaMailSender.send(message);

        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    public ResponseEntity<String> findUsernameSendMail(String email) throws MessagingException {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
        }

        if (redisUtil.existData(email)) {
            redisUtil.deleteData(email);
        }

        MimeMessage message = createMail(email);

        javaMailSender.send(message);

        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }
}
