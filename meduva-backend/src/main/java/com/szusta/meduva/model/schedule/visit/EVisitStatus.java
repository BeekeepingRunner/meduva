package com.szusta.meduva.model.schedule.visit;

public enum EVisitStatus {
    VISIT_BOOKED(1L),
    VISIT_DONE(2L),
    VISIT_CANCELLED(3L);

    private Long value;
    private EVisitStatus(Long value){
        this.value = value;
    }

    public Long getValue() {
        return this.value;
    }
}
