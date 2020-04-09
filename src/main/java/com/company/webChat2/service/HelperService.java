package com.company.webChat2.service;

import com.company.webChat2.domain.Room;
import com.company.webChat2.domain.User;
import com.company.webChat2.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HelperService {

    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private UserService userService;

    void savePublic (String[] words, Room roomPresent) {
        for (String username : words) {
            roomPresent.getUsers().add(userService.findByUsername(username));
        }
        roomRepo.save(roomPresent);
    }

    void savePrivate (String[] words, Room roomPresent) {
        int numberOfUsername = words.length;
        Set<User> numberOfUserPresent = roomPresent.getUsers();
        int usersN = numberOfUserPresent.size();

        if (usersN == 0 && (numberOfUsername==1 || numberOfUsername==2)) {
            for (String username : words) {
                roomPresent.getUsers().add(userService
                        .findByUsername(username));
            }
            roomRepo.save(roomPresent);
        }

        else if (usersN == 1 && numberOfUsername==1) {
            for (String username : words) {
                roomPresent.getUsers().add(userService
                        .findByUsername(username));
            }
            roomRepo.save(roomPresent);
        }

    }

}
