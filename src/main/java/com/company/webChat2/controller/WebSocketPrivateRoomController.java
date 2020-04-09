package com.company.webChat2.controller;

import com.company.webChat2.domain.ChatMessageForPrivateRoom;
import com.company.webChat2.domain.Role;
import com.company.webChat2.domain.User;
import com.company.webChat2.repos.ChatMessageForPrivateRoomRepo;
import com.company.webChat2.repos.RoomRepo;
import com.company.webChat2.service.ChatMessageForPrivateRoomService;
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
public class WebSocketPrivateRoomController {

    private final ChatMessageForPrivateRoomRepo chatMessageForPrivateRoomRepo;
    private final UserService userService;
    private final RoomRepo roomRepo;
    private final RoomService roomService;
    private final ChatMessageForPrivateRoomService chatMessageForPrivateRoomService;

    public WebSocketPrivateRoomController(UserService userService,
                                         ChatMessageForPrivateRoomRepo chatMessageForPrivateRoomRepo,
                                         RoomRepo roomRepo,
                                         RoomService roomService,
                                         ChatMessageForPrivateRoomService chatMessageForPrivateRoomService
    ) {
        this.userService = userService;
        this.chatMessageForPrivateRoomRepo = chatMessageForPrivateRoomRepo;
        this.roomRepo = roomRepo;
        this.roomService = roomService;
        this.chatMessageForPrivateRoomService = chatMessageForPrivateRoomService;
    }

    private String nameRoom;

    @RequestMapping("/privateRoom/{namePrivateRoom}")
    public String indexPrivateRoom(
            @AuthenticationPrincipal User user,
            Model model,
            @PathVariable(value = "namePrivateRoom") String namePrivateRoom
    ) {
        if (user.getRoles().contains(Role.BANNED_USER)) {
            return "redirect:/main";
        }
        nameRoom = namePrivateRoom;
        if (roomService.isEqualsRoom(user, nameRoom,false)) {
            model.addAttribute("username", user.getUsername());
            Iterable<ChatMessageForPrivateRoom> chatMessagesForPrivateRoom = chatMessageForPrivateRoomRepo.findAllByRoomId(roomRepo.findByName(nameRoom));
            model.addAttribute("chatMessagesForPrivateRoom", chatMessagesForPrivateRoom);
            model.addAttribute("namePrivateRoom", nameRoom);

            if (user.getRoles().contains(Role.MODERATOR) || user.getRoles().contains(Role.ADMIN)) {
                User thisUser = user;
                model.addAttribute("thisUser", thisUser);
                return "privateRoom";
            }
            return "privateRoom";
        }
        return "redirect:/main";

    }

    @SuppressWarnings({"SameReturnValue", "MVCPathVariableInspection"})
    @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    @PostMapping("/privateRoom/{nameRoom}")
    public String deleteMessage(
            @AuthenticationPrincipal User user,
            Model model,
            @RequestParam String idMessage
    ) {

        chatMessageForPrivateRoomService.deleteMessage(idMessage);
        model.addAttribute("username", user.getUsername());
        Iterable<ChatMessageForPrivateRoom> chatMessagesForPrivateRoom = chatMessageForPrivateRoomRepo.findAllByRoomId(roomRepo.findByName(nameRoom));

        model.addAttribute("chatMessagesForPrivateRoom", chatMessagesForPrivateRoom);

        if (user.getRoles().contains(Role.MODERATOR) || user.getRoles().contains(Role.ADMIN)) {
            User thisUser = user;
            model.addAttribute("thisUser", thisUser);
            return "privateRoom";
        }

        return "privateRoom";
    }

    @SuppressWarnings("unused") // используется в privateRoom.html
    @MessageMapping("/privateRoom/{namePrivateRoom}/sendMessage")
    @SendTo("/topic/privateRoom/{namePrivateRoom}")
    public ChatMessageForPrivateRoom sendMessage(
            SimpMessageHeaderAccessor headerAccessor,
            @Payload ChatMessageForPrivateRoom chatMessageForPrivateRoom
    ) {
        User user = userService.findByUsername(Objects.requireNonNull(headerAccessor.getUser()).getName());

        if(user.getRoles().contains(Role.BANNED_USER)) {
            chatMessageForPrivateRoom.setMessageToUser("Ваш аккаунт забанен");
            return chatMessageForPrivateRoom;
        }

        chatMessageForPrivateRoom.setAuthor(userService.findByUsername(chatMessageForPrivateRoom.getSender()));
        chatMessageForPrivateRoom.setRoomId(roomRepo.findByName(nameRoom));
        chatMessageForPrivateRoomRepo.save(chatMessageForPrivateRoom);
        return chatMessageForPrivateRoom;
    }

    @SuppressWarnings("unused") // используется в privateRoom.html
    @MessageMapping("/privateRoom/{namePrivateRoom}/addUser")
    @SendTo("/topic/privateRoom/{namePrivateRoom}")
    public ChatMessageForPrivateRoom addUser(
            @Payload ChatMessageForPrivateRoom chatMessageForPrivateRoom,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessageForPrivateRoom.getSender());
        return chatMessageForPrivateRoom;
    }
}
