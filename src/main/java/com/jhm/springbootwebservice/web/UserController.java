package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.dto.UserDto;
import com.jhm.springbootwebservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/auth/join")
    public String join() {
        return "join";
    }

    @PostMapping("/auth/joinProc")
    public String loginProc(UserDto userDto) {
        userService.join(userDto);

        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }
}
