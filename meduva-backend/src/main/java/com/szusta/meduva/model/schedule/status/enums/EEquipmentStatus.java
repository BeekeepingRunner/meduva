package com.szusta.meduva.model.schedule.status.enums;

public enum EEquipmentStatus {
    EQUIPMENT_OCCUPIED(1L),
    EQUIPMENT_UNAVAILABLE(2L);

    private Long value;
    private EEquipmentStatus(Long value){
        this.value = value;
    }

    public Long getValue() {
        return this.value;
    }
}
