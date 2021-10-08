package com.szusta.meduva.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "email_reset_token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name="new_email", nullable = false)
    private String email;

    public EmailResetToken(
            User user,
            String token,
            Date expiryDate,
            String email
    ) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
        this.email = email;
    }
}
