package com.insightzz.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionResponse {

    private Integer id;

    private String permissionCode;

    private String module;

    private String action;

    private String description;
}