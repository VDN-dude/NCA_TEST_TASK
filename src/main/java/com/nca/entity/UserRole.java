package com.nca.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    ROLE_ADMIN,
    ROLE_JOURNALIST,
    ROLE_SUBSCRIBER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
