package com.jhm.springbootwebservice.web;


import com.jhm.springbootwebservice.config.auth.dto.FindPasswordRequestDto;
import com.jhm.springbootwebservice.service.user.UserService;
import com.jhm.springbootwebservice.validation.CheckFindUsernameValidator;
import com.jhm.springbootwebservice.config.auth.dto.FindUsernameRequestDto;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FindUsernameController {

    private final UserService userService;
    private final CheckFindUsernameValidator checkFindUsernameValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkFindUsernameValidator);
    }

    @GetMapping("/auth/find-username1")
    public String findUsername1() {
        return "find-username1";
    }

    @GetMapping("/auth/find-username2")
    public String findUsername2() {
        return "find-username2";
    }

    @PostMapping("/auth/findUsername")
    public String findUsername(@Valid FindUsernameRequestDto userDto, Errors errors, Model model, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            // 회원가입 실패 시 입력 데이터값 유지
            model.addAttribute("userDto", userDto);

            // 유효성 통과 못한 필드와 메시지 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/find-username1";
        }

        String username = userService.findUsername(userDto);

        redirectAttributes.addAttribute("username", username);

        return "redirect:/auth/find-username2";
    }

}