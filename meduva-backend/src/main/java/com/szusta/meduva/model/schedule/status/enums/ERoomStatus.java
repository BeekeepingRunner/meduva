package com.szusta.meduva.model.schedule.status.enums;

public enum ERoomStatus {
    ROOM_OCCUPIED(1L),
    ROOM_UNAVAILABLE(2L);

    private Long value;
    private ERoomStatus(Long value){
        this.value = value;
    }

    public Long getValue() {
        return this.value;
    }
}
