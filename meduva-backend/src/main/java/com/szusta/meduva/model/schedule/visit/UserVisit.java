package com.szusta.meduva.model.schedule.visit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_visit")
@Getter
@Setter
@NoArgsConstructor
public class UserVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "as_client")
    private boolean asClient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visit_id")
    @JsonIgnore
    private Visit visit;

    public UserVisit(User user, boolean asClient) {
        this.asClient = asClient;
        this.user = user;
    }
}
