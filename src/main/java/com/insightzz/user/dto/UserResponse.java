package com.insightzz.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {

    private Long userId;

    private Integer roleId;

    private String userRole;

    //private String userName;
    private String employeId;

    private String department;

    private String password;

    private String fullName;

    //private String firstName;

   // private String lastName;

    private String userEmail;

    private String userMobNo;

    private String userDesignation;

    private LocalDate userDoj;

    private LocalDate userDol;

    private Boolean isActive;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;
}
