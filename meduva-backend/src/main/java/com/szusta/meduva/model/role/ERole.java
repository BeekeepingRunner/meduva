package com.szusta.meduva.model.role;

public enum ERole {
    ROLE_CLIENT(1L),
    ROLE_WORKER(2L),
    ROLE_RECEPTIONIST(3L),
    ROLE_ADMIN(4L);

    private Long value;
    private ERole(Long value){
        this.value = value;
    }

    public Long getValue() {
        return this.value;
    }
}
