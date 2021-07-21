package com.rafaelgude.customersfavproducts.enums;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String role;

    private Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Role toEnum(String role) {
        if (role == null || role.isBlank())
            return null;

        for (Role x : Role.values())
            if (x.getRole().equals(role))
                return x;

        throw new IllegalArgumentException("Invalid role: " + role);
    }

}
