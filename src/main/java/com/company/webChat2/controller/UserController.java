package com.company.webChat2.controller;

import com.company.webChat2.domain.Role;
import com.company.webChat2.domain.User;
import com.company.webChat2.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("SameReturnValue")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @SuppressWarnings("SameReturnValue")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @SuppressWarnings("SameReturnValue")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());

        return "profile";
    }

    @SuppressWarnings("SameReturnValue")
    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String username,
            Model model
    ) {
        if (userService.isUsernameExist(username))
        {
            model.addAttribute("usernameIsExist", "Данное имя уже занято, введите другое");
            model.addAttribute("username", user.getUsername());
            return "profile";
        }

        userService.updateProfile(user, password, username);

        return "redirect:/user/profile";
    }
}