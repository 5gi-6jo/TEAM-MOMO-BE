package com.sparta.team6.momo.model;

import lombok.Getter;

public enum UserRole {
    ROLE_USER(Role.USER),
    ROLE_GUEST(Role.GUEST);

    @Getter
    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public static class Role {
        public static final String USER = "ROLE_USER";
        public static final String GUEST = "ROLE_GUEST";
    }

}
