package com.szusta.meduva.payload.request;

import com.szusta.meduva.model.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeRoleRequest {

    //@NotNull(message = "Role id cannot be NULL")
    private Long roleId;

}
