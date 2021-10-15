package com.szusta.meduva.payload.request;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.Service;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AssignServicesToWorkerRequest {

    //@NotNull(message = "There are no services")
    private Long[] servicesId;

}
