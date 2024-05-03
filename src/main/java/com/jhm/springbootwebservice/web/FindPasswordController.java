package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.dto.FindPasswordRequestDto;
import com.jhm.springbootwebservice.service.user.UserService;
import com.jhm.springbootwebservice.validation.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FindPasswordController {

    private final UserService userService;
    private final CheckUsernameFindPasswordValidator checkUsernameFindPasswordValidator;
    private final CheckEmailFindPasswordValidator checkEmailFindPasswordValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUsernameFindPasswordValidator);
        binder.addValidators(checkEmailFindPasswordValidator);
    }

    @GetMapping("/auth/find-password")
    public String findPassword() {
        return "find-password";
    }

    @PostMapping("/auth/findPassword")
    public String findPassword(@Valid FindPasswordRequestDto userDto, Errors errors, Model model) throws MessagingException {
        if (errors.hasErrors()) {
            // 회원가입 실패 시 입력 데이터값 유지
            model.addAttribute("userDto", userDto);

            // 유효성 통과 못한 필드와 메시지 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "find-password";
        }

        userService.findPassword(userDto);

        return "redirect:/";
    }
}
