package com.szusta.meduva.payload.request.add;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NewServiceRequest {

    private String name;
    private String description;
    private int durationInMin;
    private BigDecimal price;
    private boolean itemless;
    private boolean deleted;
}
