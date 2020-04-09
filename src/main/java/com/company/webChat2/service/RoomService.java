package com.company.webChat2.service;

import com.company.webChat2.domain.Room;
import com.company.webChat2.domain.User;
import com.company.webChat2.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoomService {

    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private HelperService helperService;

    public void deleteRoom(String id) {
        Optional<Room> room = roomRepo.findById(Long.valueOf(id));

        if (room.isPresent()) {
            Room roomForDelete = room.get();
            roomRepo.delete(roomForDelete);
        }
        else {
            System.out.println("Комната не найдена");
        }

    }

    public List<Room> userPrivateRooms (User user) {
        Iterable<Room> roomsOfUser = user.getRooms();
        ArrayList<Room> privateRooms = new ArrayList<>();
        for (Room r : roomsOfUser) {
            if (r.getIsPrivate())
                privateRooms.add(r);
        }

        return privateRooms;
    }

    public List<Room> userPublicRooms(User user) {
        Iterable<Room> roomsOfUser = user.getRooms();
        ArrayList<Room> publicRoomsOfUser = new ArrayList<>();
        for (Room r : roomsOfUser) {
            if (!r.getIsPrivate())
                publicRoomsOfUser.add(r);
        }

        return publicRoomsOfUser;
    }

    public void createRoom (Room room, User user, String usersList, boolean isPrivate) {
        room.getUsers().add(user);
        String[] words = usersList.split("\\p{P}?[ \\t\\n\\r]+");

        if (!isPrivate) {
            for (String username : words) {
                room.getUsers().add(userService.findByUsername(username));
            }
        }
        else {
            int numberOfUsername = words.length;
            if (numberOfUsername<=1) {
                for (String username : words) {
                    room.getUsers().add(userService.findByUsername(username));
                }
            }
        }
    }

    public void addUserInRoom(String nameOfRoom, String newUsers) {
        Room roomPresent = roomRepo.findByName(nameOfRoom);
        String[] words = newUsers.split("\\p{P}?[ \\t\\n\\r]+");

        if (!roomPresent.getIsPrivate()) {
            helperService.savePublic(words, roomPresent);
        }

        else {

            helperService.savePrivate(words, roomPresent);
        }

    }

    public void deleteUserFromRoom(String nameOfRoom, String deleteUsers) {
        Room roomPresent = roomRepo.findByName(nameOfRoom);

        String[] words = deleteUsers.split("\\p{P}?[ \\t\\n\\r]+");
        for (String username : words) {
            roomPresent.getUsers().remove(userService.findByUsername(username));
        }

        roomRepo.save(roomPresent);
    }

    public boolean isEqualsRoom (User user, String nameRoom, boolean equalsRoom) {

        Set<Room> roomsThisUser = user.getRooms();
        for (Room r: roomsThisUser) {
            if (!equalsRoom) {
                equalsRoom = r.getName().equals(nameRoom);
            }

        }

        return equalsRoom;
    }

}
