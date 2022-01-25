package com.szusta.meduva.model.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
public class Undeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    protected boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void markAsDeleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
}
