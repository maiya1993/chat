package com.company.webChat2.controller;

import com.company.webChat2.domain.Role;
import com.company.webChat2.domain.Room;
import com.company.webChat2.domain.User;
import com.company.webChat2.repos.RoomRepo;
import com.company.webChat2.service.ChatMessageForPrivateRoomService;
import com.company.webChat2.service.ChatMessageService;
import com.company.webChat2.service.RoomService;
import com.company.webChat2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@SuppressWarnings({"ALL", "SameReturnValue"})
@Controller
public class RoomController {

    @Autowired
    ChatMessageForPrivateRoomService chatMessageForPrivateRoomService;
    @Autowired
    ChatMessageService chatMessageService;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/newRoom")
    public String main(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        if (user.getRoles().contains(Role.BANNED_USER)) {
            return "redirect:/main";
        }

        model.addAttribute("rooms", roomRepo.findByIsPrivate(true));
        model.addAttribute("publicRooms", roomRepo.findByIsPrivate(false));
        model.addAttribute("users", userService.findAll());
        //все приватные комнаты, в которых состоит пользователь:
        model.addAttribute("privateRooms", roomService.userPrivateRooms(user));
        //все публичные комнаты, в которых состоит пользователь:
        model.addAttribute("publicRoomsOfUser", roomService.userPublicRooms(user));

        return "newRoom";
    }

    @PostMapping("/newRoom")
    public String createRoom(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String usersList,
            @RequestParam boolean isPrivate, Map<String, Object> model
    ) {
        Room room = new Room(name, isPrivate, user);
        roomService.createRoom(room, user, usersList, isPrivate);
        Room checkRoom = roomRepo.findByName(name);
        if (checkRoom != null) {
            if (name.equals(checkRoom.getName()) && !name.isEmpty()) {
                model.put("ifNameRoomExist", "Это название уже занято. Введите другое");
                model.put("rooms", roomRepo.findByIsPrivate(true));
                model.put("publicRooms", roomRepo.findByIsPrivate(false));
                model.put("users", userService.findAll());
                model.put("privateRooms", roomService.userPrivateRooms(user));
                model.put("publicRoomsOfUser", roomService.userPublicRooms(user));
                return "newRoom";
            }

        } else {
            roomRepo.save(room);
        }
        model.put("rooms", roomRepo.findByIsPrivate(true));
        model.put("publicRooms", roomRepo.findByIsPrivate(false));
        model.put("users", userService.findAll());
        model.put("privateRooms", roomService.userPrivateRooms(user));
        model.put("publicRoomsOfUser", roomService.userPublicRooms(user));
        return "newRoom";
    }

    @PostMapping("deleteRoom")
    public String deleteMyRoom(
            @AuthenticationPrincipal User user,
            String idRoom,
            Map<String, Object> model
    ) {
        chatMessageForPrivateRoomService.deleteMessagesByRoomId(idRoom);
        chatMessageService.deleteMessagesByRoomId(idRoom);
        roomService.deleteRoom(idRoom);

        model.put("rooms", roomRepo.findByIsPrivate(true));
        model.put("publicRooms", roomRepo.findByIsPrivate(false));
        model.put("users", userService.findAll());
        model.put("privateRooms", roomService.userPrivateRooms(user));
        model.put("publicRoomsOfUser", roomService.userPublicRooms(user));

        return "redirect:/newRoom";
    }

    @PostMapping("roomRename")
    public String roomRename(
            @AuthenticationPrincipal User user,
            @RequestParam String oldRoom,
            @RequestParam String newRoom,
            Map<String, Object> model
    ) {
        Room roomToRename = roomRepo.findByName(oldRoom);
        Room checkSecondExistsRoom = roomRepo.findByName(newRoom);
        if (checkSecondExistsRoom != null) {
            model.put("errorName", "Ошибка. Данное имя уже существует");
            model.put("rooms", roomRepo.findByIsPrivate(true));
            model.put("publicRooms", roomRepo.findByIsPrivate(false));
            model.put("users", userService.findAll());
            model.put("privateRooms", roomService.userPrivateRooms(user));
            model.put("publicRoomsOfUser", roomService.userPublicRooms(user));
            return "newRoom";
        } else {
            roomToRename.setName(newRoom);
            roomRepo.save(roomToRename);
        }
        model.put("rooms", roomRepo.findByIsPrivate(true));
        model.put("publicRooms", roomRepo.findByIsPrivate(false));
        model.put("users", userService.findAll());
        model.put("privateRooms", roomService.userPrivateRooms(user));
        model.put("publicRoomsOfUser", roomService.userPublicRooms(user));
        return "redirect:/newRoom";
    }

    @PostMapping("addUserInRoom")
    public String addUserInRoom(
            @AuthenticationPrincipal User user,
            @RequestParam String nameOfRoom,
            @RequestParam String newUsers,
            Map<String, Object> model
    ) {
        roomService.addUserInRoom(nameOfRoom, newUsers);

        model.put("rooms", roomRepo.findByIsPrivate(true));
        model.put("publicRooms", roomRepo.findByIsPrivate(false));
        model.put("users", userService.findAll());
        model.put("privateRooms", roomService.userPrivateRooms(user));
        model.put("publicRoomsOfUser", roomService.userPublicRooms(user));

        return "redirect:/newRoom";
    }

    @PostMapping("deleteUserFromRoom")
    public String deleteUserFromRoom(
            @AuthenticationPrincipal User user,
            @RequestParam String nameOfRoom,
            @RequestParam String deleteUsers,
            Map<String, Object> model
    ) {
        roomService.deleteUserFromRoom(nameOfRoom, deleteUsers);

        model.put("rooms", roomRepo.findByIsPrivate(true));
        model.put("publicRooms", roomRepo.findByIsPrivate(false));
        model.put("users", userService.findAll());
        model.put("privateRooms", roomService.userPrivateRooms(user));
        model.put("publicRoomsOfUser", roomService.userPublicRooms(user));

        return "redirect:/newRoom";
    }

}