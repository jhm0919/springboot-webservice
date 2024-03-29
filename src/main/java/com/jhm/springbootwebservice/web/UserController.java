package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.config.auth.dto.UserRequestDto;
import com.jhm.springbootwebservice.service.user.UserService;
import com.jhm.springbootwebservice.validation.CheckEmailValidator;
import com.jhm.springbootwebservice.validation.CheckNameValidator;
import com.jhm.springbootwebservice.validation.CheckPasswordEqualValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final CheckEmailValidator checkEmailValidator;
    private final CheckNameValidator checkNameValidator;
    private final CheckPasswordEqualValidator checkPasswordEqualValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkEmailValidator);
        binder.addValidators(checkNameValidator);
        binder.addValidators(checkPasswordEqualValidator);
    }

    @GetMapping("/auth/join")
    public String join() {
        return "join";
    }

    @PostMapping("/auth/joinProc")
    public String loginProc(@Valid UserRequestDto userDto, Errors errors, Model model) {

        if (errors.hasErrors()) {
            // 회원가입 실패 시 입력 데이터값 유지
            model.addAttribute("userDto", userDto);

            // 유효성 통과 못한 필드와 메시지 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            // 회원가입 페이지로 다시 리턴
            return "/join";
        }

        userService.join(userDto);

        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login";
    }

    @GetMapping("/update")
    public String update(@LoginUser SessionUser sessionUser, Model model) {

        if (sessionUser != null) {
            model.addAttribute("name", sessionUser.getName());
            model.addAttribute("sessionUserDto", sessionUser);
        }

        return "user-update";
    }
}
