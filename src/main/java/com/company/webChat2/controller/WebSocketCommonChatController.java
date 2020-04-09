package com.company.webChat2.controller;

import com.company.webChat2.domain.CommonChatMessage;
import com.company.webChat2.domain.Role;
import com.company.webChat2.domain.User;
import com.company.webChat2.repos.CommonChatMessageRepo;
import com.company.webChat2.service.CommonChatMessageService;
import com.company.webChat2.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@SuppressWarnings({"UnnecessaryLocalVariable"})
@Controller
public class WebSocketCommonChatController {


    private final UserService userService;
    private final CommonChatMessageRepo commonChatMessageRepo;
    private final CommonChatMessageService commonChatMessageService;

    public WebSocketCommonChatController(UserService userService,
                                         CommonChatMessageRepo commonChatMessageRepo,
                                         CommonChatMessageService commonChatMessageService
    ) {
        this.userService = userService;
        this.commonChatMessageRepo = commonChatMessageRepo;
        this.commonChatMessageService = commonChatMessageService;
    }

    @SuppressWarnings("SameReturnValue")
    @RequestMapping("/commonChat")
    public String commonChatMessage(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("username", user.getUsername());

        Iterable<CommonChatMessage> commonChatMessages = commonChatMessageRepo.findAll();
        model.addAttribute("commonChatMessages", commonChatMessages);
        if (user.getRoles().contains(Role.MODERATOR) || user.getRoles().contains(Role.ADMIN)) {
            User thisUser = user;
            model.addAttribute("thisUser", thisUser);
            return "commonChat";
        }
        return "commonChat";
    }

    @SuppressWarnings("SameReturnValue")
    @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    @PostMapping("commonChat")
    public String deleteCommonChatMessage(
            @AuthenticationPrincipal User user,
            Model model,
            @RequestParam String idCommonChatMessage
    ) {

        commonChatMessageService.deleteMessage(idCommonChatMessage);

        model.addAttribute("username", user.getUsername());
        Iterable<CommonChatMessage> commonChatMessages = commonChatMessageRepo.findAll();
        model.addAttribute("commonChatMessages", commonChatMessages);
        if (user.getRoles().contains(Role.MODERATOR) || user.getRoles().contains(Role.ADMIN)) {
            User thisUser = user;
            model.addAttribute("thisUser", thisUser);
            return "commonChat";
        }

        return "commonChat";
    }

    @SuppressWarnings("unused") // используется в commonChat.html
    @MessageMapping("/commonChat.sendMessage")
    @SendTo("/topic/commonChat")
    public CommonChatMessage sendMessage(
            SimpMessageHeaderAccessor headerAccessor,
            @Payload CommonChatMessage commonChatMessage
    ) {
        User user = userService.findByUsername(Objects.requireNonNull(headerAccessor.getUser()).getName());

        if (user.getRoles().contains(Role.BANNED_USER)) {
            commonChatMessage.setMessageToUser("Ваш аккаунт забанен");
            return commonChatMessage;
        }

        commonChatMessage.setAuthor(user);
        commonChatMessageRepo.save(commonChatMessage);
        return commonChatMessage;
    }

    @SuppressWarnings("unused") // используется в commonChat.html
    @MessageMapping("/commonChat.addUser")
    @SendTo("/topic/commonChat")
    public CommonChatMessage addUser(
            @Payload CommonChatMessage commonChatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", commonChatMessage.getSender());
        return commonChatMessage;
    }


}