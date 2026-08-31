package com.insightzz.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSuggestionResponse {

    private Long userId;

    private String employeId;

    private String firstName;

    private String lastName;

    private String userEmail;

    private String department;
}