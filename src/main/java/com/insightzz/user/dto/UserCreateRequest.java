package com.insightzz.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "User name is required")
    @Size(
            min = 3,
            max = 100,
            message = "User name must be between 3 and 100 characters"
    )
    private String userName;

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

    private String userRole;
}
