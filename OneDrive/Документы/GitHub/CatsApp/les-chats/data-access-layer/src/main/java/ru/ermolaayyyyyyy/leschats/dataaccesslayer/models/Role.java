package ru.ermolaayyyyyyy.leschats.dataaccesslayer.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_UNAUTHORIZED;

    @Override
    public String getAuthority() {
        return name();
    }
    public static Role findRole(String role){
        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                return r;
            }
        }
        return null;
    }
}
