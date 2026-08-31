package com.insightzz.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "Employe ID is required")
    @Size(
            min = 3,
            max = 10,
            message = "Employe ID must be between 3 and 10 characters"
    )
    private String employeId;

    @NotBlank(message = "Department is required")
    @Size(
            min = 1,
            max = 50,
            message = "Department must be between 3 and 50 characters"
    )
    private String department;

    @NotBlank(message = "First name is required")
    @Size(
            min = 3,
            max = 100,
            message = "First name must be between 3 and 100 characters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(
            min = 3,
            max = 100,
            message = "Last name must be between 3 and 100 characters"
    )
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(
            min = 5,
            max = 100,
            message = "Password must be between 5 and 100 characters"
    )
    private String password;

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    @Size(max = 254, message = "Email cannot exceed 254 characters")
    private String userEmail;

    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Mobile number must contain 10 to 15 digits"
    )
    private String userMobNo;

    @Size(
            max = 100,
            message = "Designation cannot exceed 100 characters"
    )
    private String userDesignation;

    private LocalDate userDoj;

    private LocalDate userDol;

    @NotNull
    private Integer roleId;
}
