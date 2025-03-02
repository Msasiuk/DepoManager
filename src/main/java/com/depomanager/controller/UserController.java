package com.depomanager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal User user) {
        return "Bienvenido, " + user.getUsername() + "!";
    }
}
