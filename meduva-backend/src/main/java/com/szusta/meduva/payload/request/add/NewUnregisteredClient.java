package com.szusta.meduva.payload.request.add;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUnregisteredClient {

    private String name;
    private String surname;
    private String phoneNumber;
}
