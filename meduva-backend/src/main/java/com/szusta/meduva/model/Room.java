package com.szusta.meduva.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends Undeletable {

    private String name;
    private String description;

    public Room(String name,
                String description,
                boolean deleted) {
        this.name = name;
        this.description = description;
        this.deleted = deleted;
    }
}
