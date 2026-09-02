package com.insightzz.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                //@Index(name = "idx_users_username", columnList = "user_name"),
                @Index(name = "idx_users_email", columnList = "user_email"),
                //@Index(name = "idx_users_role", columnList = "user_role")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "role_id",
            nullable = false
    )
    private Role role;

    @Column(
            name = "employe_id",
            nullable = false,
            length = 20
    )
    private String employeId;

    @Column(
            name = "department",
            nullable = false,
            length = 20
    )
    private String department;

    @Column(
            name = "first_name",
            nullable = false,
            length = 20
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 20
    )
    private String lastName;

    @Column(
            name = "password",
            nullable = false,
            length = 255
    )
    private String password;

    @Column(
            name = "user_email",
            nullable = false,
            unique = true,
            length = 50
    )
    private String userEmail;

    @Column(
            name = "user_mob_no",
            length = 15
    )
    private String userMobNo;

    @Column(
            name = "user_designation",
            length = 30
    )
    private String userDesignation;

    @Column(name = "user_doj")
    private LocalDate userDoj;

    @Column(name = "user_dol")
    private LocalDate userDol;

    @Column(
            name = "is_active",
            nullable = false
    )
    private Boolean isActive;

    @Column(
            name = "create_datetime",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createDatetime;

    @Column(
            name = "update_datetime",
            nullable = false
    )
    private LocalDateTime updateDatetime;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createDatetime = now;
        this.updateDatetime = now;

        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        this.updateDatetime = LocalDateTime.now();
    }
    @Column(name = "token_version", nullable = false)
    private Long tokenVersion;

}
