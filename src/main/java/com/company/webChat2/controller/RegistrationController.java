package com.company.webChat2.controller;

import com.company.webChat2.domain.User;
import com.company.webChat2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController (UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        if (!userService.addUser(user)) {
            model.put("message", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/login")
    public String auth(
            @RequestParam(name = "error", required = false, defaultValue="default") String name,
            Map<String, Object> model
    ) {
        if (name.equals("true")) {
            model.put("userIsNo", "Пользователя с такими данными не существует!");
            return "login";
        }

        return "login";
    }

}