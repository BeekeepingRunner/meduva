package com.szusta.meduva.model;

import com.szusta.meduva.model.common.Undeletable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table( name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountlessClient extends Undeletable {

    private String name;
    private String surname;
    @Column(name = "phone_number")
    private String phoneNumber;
}
