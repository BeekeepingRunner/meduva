package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class NewEqModelRequest {

    @NotNull
    String modelName;

    @NotNull
    @Min(1)
    int itemCount;

    @NotNull
    List<Long> selectedRoomsIds;

    @NotNull
    List<Long> servicesIds;
}
