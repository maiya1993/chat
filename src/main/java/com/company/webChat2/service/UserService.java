package com.company.webChat2.service;

import com.company.webChat2.domain.User;
import com.company.webChat2.domain.Role;
import com.company.webChat2.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public User findByUsername(String name) {
        return userRepo.findByUsername(name);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String username) {

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        if (!username.isEmpty() && !user.getUsername().equals(username))
            user.setUsername(username);

        userRepo.save(user);

    }

    public boolean isDigit(String time) throws NumberFormatException {
        try {
            Integer.parseInt(time);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void updateBannedUser(User user) {

        user.getRoles().clear();
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

    }

    public boolean isUsernameExist(String username) {
        if (userRepo.findByUsername(username) == null) {
            return false;
        }

        return username.equals(userRepo.findByUsername(username)
                .getUsername()) && !username.isEmpty();

    }
}