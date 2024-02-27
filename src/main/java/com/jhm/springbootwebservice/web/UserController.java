package com.jhm.springbootwebservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/auth/login")
    public String index() {
        return "login";
    }
}
