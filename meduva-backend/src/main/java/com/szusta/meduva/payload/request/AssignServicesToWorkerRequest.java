package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignServicesToWorkerRequest {

    //@NotNull(message = "There are no services")
    private Long[] servicesId;

}
