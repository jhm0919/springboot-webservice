package com.jhm.springbootwebservice.service.email;

import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.domain.validation.EmailValidation;
import com.jhm.springbootwebservice.domain.validation.EmailValidationRepository;
import com.jhm.springbootwebservice.web.dto.request.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final EmailValidationRepository emailValidationRepository;
    public static HashMap<String, String> codeStorage = new HashMap<>();
    private final ApplicationEventPublisher publisher;
    private static String number;

    public static void createNumber() {
        number = "" + (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    private MimeMessage createMail(String email) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(email);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
            codeStorage.remove(email);
            System.out.println(codeStorage);
        }

        return message;
    }

    public ResponseEntity<String> sendMail(String email) {
        MimeMessage message = createMail(email);
        if (!codeStorage.containsKey(email)) {
            javaMailSender.send(message);
            codeStorage.put(email, number);
            System.out.println(codeStorage);
            publisher.publishEvent(new EmailSendApplicationEvent(this, email, number));
        } else {
            log.info("3분이 지나지 않았으므로 전송 불가");
            return ResponseEntity.badRequest().body("3분이 지나지 않았으므로 전송 불가");
        }

        // 이메일 검증 entity저장
        EmailValidation entity = EmailValidation.builder()
                .email(email)
                .code(number)
                .confirm(false)
                .build();
        emailValidationRepository.save(entity);
        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    public boolean existsByEmail(String email) {
        boolean result = userRepository.existsByEmail(email);
        return result;
    }

    public boolean confirmEmail(EmailDto dto) {
        String email = dto.getEmail();
        String code = dto.getCode();
        String findCode = codeStorage.get(email);
        log.info("이메일과 코드가 일치하는지 확인");
        EmailValidation entity = emailValidationRepository.findByEmail(email);
        if (code.equals(findCode)) {
            entity.update(true);
            emailValidationRepository.save(entity);
            log.info("일치");
            codeStorage.remove(email);
            System.out.println(codeStorage);
            return true;
        }
        log.info("불일치");
        System.out.println(codeStorage);

        return false;
    }
}
