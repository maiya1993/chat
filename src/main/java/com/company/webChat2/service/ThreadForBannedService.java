package com.company.webChat2.service;

import com.company.webChat2.domain.Role;
import com.company.webChat2.domain.User;

@SuppressWarnings("CanBeFinal")
public class ThreadForBannedService implements Runnable {

    private User user;
    private UserService userService;
    private String time;

    public ThreadForBannedService(User user, UserService userService, String time) {
        this.user = user;
        this.userService = userService;
        this.time = time;
    }

    public void run() {
        Long bannedTime = Long.valueOf(time);
        if (user.getRoles().contains(Role.BANNED_USER)) {
            try {
                Thread.sleep(60000 * bannedTime);
            } catch (InterruptedException e) {
                System.out.println("Thread has been interrupted");
            }

            userService.updateBannedUser(user);
        }

    }
}