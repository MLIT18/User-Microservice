package com.insightzz.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleResponse {

    private Integer id;
    private String roleName;
    private String description;
}
