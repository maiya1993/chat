package com.company.webChat2.controller;

import com.company.webChat2.domain.Role;
import com.company.webChat2.domain.User;
import com.company.webChat2.service.ThreadForBannedService;
import com.company.webChat2.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/banneduser")
public class BannedUserController {

    private final UserService userService;

    public BannedUserController (UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("SameReturnValue")
    @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    @GetMapping
    public String userList(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        if (!(user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.MODERATOR))) {
            return "redirect:/main";
        }
        model.addAttribute("users", userService.findAll());

        return "bannedUserList";
    }

    @SuppressWarnings("SameReturnValue")
    @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        if (user.getRoles().contains(Role.MODERATOR) || user.getRoles().contains(Role.ADMIN)) {
            return "bannedUserEdit";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "bannedUserEdit";
        }

    }

    @SuppressWarnings("SameReturnValue")
    @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user,
            @RequestParam String time,
            Model model

    ) {
        if (userService.isDigit(time)) {
            userService.saveUser(user, username, form);
            Thread threadForBanned = new Thread(new ThreadForBannedService(user, userService, time), "ThreadForBanned");
            threadForBanned.start();

            return "redirect:/banneduser";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("notNumber", "Введено неверное значение. Повторите попытку");
        return "bannedUserEdit";
    }

}