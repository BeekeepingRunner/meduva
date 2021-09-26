package com.szusta.meduva.model.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    Collection<User> users;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
