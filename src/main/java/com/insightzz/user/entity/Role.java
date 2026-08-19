package com.insightzz.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "role_name",
            nullable = false,
            unique = true,
            length = 100
    )
    private String roleName;

    @Column(length = 255)
    private String description;

    @Column(
            name = "is_active",
            nullable = false
    )
    private Boolean isActive;
}
