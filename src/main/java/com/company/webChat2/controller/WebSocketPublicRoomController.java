package com.company.webChat2.controller;

import com.company.webChat2.domain.ChatMessage;
import com.company.webChat2.domain.Role;
import com.company.webChat2.domain.User;
import com.company.webChat2.repos.ChatMessageRepo;
import com.company.webChat2.repos.RoomRepo;
import com.company.webChat2.service.ChatMessageService;
import com.company.webChat2.service.RoomService;
import com.company.webChat2.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@SuppressWarnings({"UnnecessaryLocalVariable"})
@Controller
public class WebSocketPublicRoomController {

    private final ChatMessageRepo chatMessageRepo;
    private final UserService userService;
    private final RoomRepo roomRepo;
    private final RoomService roomService;
    private final ChatMessageService chatMessageService;

    public WebSocketPublicRoomController(UserService userService,
                                         ChatMessageRepo chatMessageRepo,
                                          RoomRepo roomRepo,
                                          RoomService roomService,
                                         ChatMessageService chatMessageService
    ) {
        this.userService = userService;
        this.chatMessageRepo = chatMessageRepo;
        this.roomRepo = roomRepo;
        this.roomService = roomService;
        this.chatMessageService = chatMessageService;
    }

    private String nameRoom;

    @RequestMapping("/chat/{namePublicRoom}")
    public String indexPublicRoom(
            @AuthenticationPrincipal User user,
            Model model,
            @PathVariable(value = "namePublicRoom") String namePublicRoom
    ) {
        if (user.getRoles().contains(Role.BANNED_USER)) {
            return "redirect:/main";
        }
        nameRoom = namePublicRoom;
        if (roomService.isEqualsRoom(user, nameRoom,false)) {
            model.addAttribute("username", user.getUsername());
            Iterable<ChatMessage> chatMessages = chatMessageRepo.findAllByRoomId(roomRepo.findByName(nameRoom));
            model.addAttribute("chatMessages", chatMessages);
            model.addAttribute("namePublicRoom", nameRoom);
            if (user.getRoles().contains(Role.MODERATOR) || user.getRoles().contains(Role.ADMIN)) {
                User thisUser = user;
                model.addAttribute("thisUser", thisUser);
                return "chat";
            }

            return "chat";
        }

        return "redirect:/main";
    }

    @SuppressWarnings({"SameReturnValue", "MVCPathVariableInspection"})
    @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    @PostMapping("/chat/{nameRoom}")
    public String deleteMessage(
            @AuthenticationPrincipal User user,
            Model model,
            @RequestParam String idMessage
    ) {

        chatMessageService.deleteMessage(idMessage);
        model.addAttribute("username", user.getUsername());
        Iterable<ChatMessage> chatMessages = chatMessageRepo.findAllByRoomId(roomRepo.findByName(nameRoom));
        model.addAttribute("chatMessages", chatMessages);
        if (user.getRoles().contains(Role.MODERATOR) || user.getRoles().contains(Role.ADMIN)) {
            User thisUser = user;
            model.addAttribute("thisUser", thisUser);
            return "chat";
        }

        return "chat";
    }

    @SuppressWarnings("unused") // используется в chat.html
    @MessageMapping("/chat/{namePublicRoom}/sendMessage")
    @SendTo("/topic/publicChatRoom/{namePublicRoom}")
    public ChatMessage sendMessage(
            SimpMessageHeaderAccessor headerAccessor,
            @Payload ChatMessage chatMessage
    ) {
        User user = userService.findByUsername(Objects.requireNonNull(headerAccessor.getUser()).getName());

        if(user.getRoles().contains(Role.BANNED_USER)) {
            chatMessage.setMessageToUser("Ваш аккаунт забанен");
            return chatMessage;
        }

        chatMessage.setAuthor(userService.findByUsername(chatMessage.getSender()));
        chatMessage.setRoomId(roomRepo.findByName(nameRoom));
        chatMessageRepo.save(chatMessage);
        return chatMessage;
    }

    @SuppressWarnings("unused") // используется в chat.html
    @MessageMapping("/chat/{namePublicRoom}/addUser")
    @SendTo("/topic/publicChatRoom/{namePublicRoom}")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        return chatMessage;
    }

}