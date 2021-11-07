package com.szusta.meduva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.common.ScheduleSubject;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.model.schedule.visit.UserVisit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "login"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class User extends ScheduleSubject {

    private String login;
    private String email;
    private String password;

    private String name;
    private String surname;
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

/*
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Visit> visits;
 */

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserVisit> userVisits;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<WorkerSchedule> workerSchedules;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "worker_service",
            joinColumns = @JoinColumn(name = "worker_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services = new HashSet<>();


    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public User(String login, String email, String password, String name, String surname, String phoneNumber) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.deleted = false;
    }

    public boolean canPerform(Service service) {
        return services.contains(service);
    }

    public void addUserVisit(UserVisit userVisit) {
        if (userVisits == null) {
            userVisits = new ArrayList<>();
        }
        userVisits.add(userVisit);
    }
}
