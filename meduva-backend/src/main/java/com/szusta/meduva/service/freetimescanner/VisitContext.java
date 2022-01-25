package com.szusta.meduva.service.freetimescanner;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitContext {

    private User worker;
    private Service service;
    private List<Room> suitableRooms;

    public VisitContext(User worker, Service service) {
        this.worker = worker;
        this.service = service;
    }
}
