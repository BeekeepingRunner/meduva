package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewRoomRequest {

    private String name;
    private String description;
    private boolean deleted;
}
