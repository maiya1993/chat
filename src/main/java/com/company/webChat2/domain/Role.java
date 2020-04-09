package com.company.webChat2.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, MODERATOR, BANNED_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}