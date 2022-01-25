package com.szusta.meduva.model.schedule.status.enums;

public enum EWorkerStatus {
    WORKER_OCCUPIED(1L),
    WORKER_ABSENT(2L);

    private Long value;
    private EWorkerStatus(Long value){
        this.value = value;
    }

    public Long getValue() {
        return this.value;
    }
}
