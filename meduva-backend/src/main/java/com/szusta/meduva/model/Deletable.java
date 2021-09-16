package com.szusta.meduva.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Deletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
