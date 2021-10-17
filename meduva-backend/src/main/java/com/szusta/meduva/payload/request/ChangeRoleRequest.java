package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleRequest {

    //@NotNull(message = "Role id cannot be NULL")
    private Long roleId;

}
