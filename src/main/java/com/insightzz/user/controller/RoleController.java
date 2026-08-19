package com.insightzz.user.controller;

import com.insightzz.user.dto.PermissionResponse;
import com.insightzz.user.dto.RoleResponse;
import com.insightzz.user.repository.RolePermissionRepository;
import com.insightzz.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<List<RoleResponse>> getRoles() {

        List<RoleResponse> roles =
                roleRepository.findByIsActiveTrue()
                        .stream()
                        .map(role ->
                                RoleResponse.builder()
                                        .id(role.getId())
                                        .roleName(role.getRoleName())
                                        .description(
                                                role.getDescription()
                                        )
                                        .build()
                        )
                        .toList();

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<List<PermissionResponse>>
    getRolePermissions(
            @PathVariable Integer roleId) {

        List<PermissionResponse> permissions =
                rolePermissionRepository
                        .findByRoleId(roleId)
                        .stream()
                        .filter(rp ->
                                Boolean.TRUE.equals(
                                        rp.getPermission().getIsActive()
                                )
                        )
                        .map(rp ->
                                PermissionResponse.builder()
                                        .id(
                                                rp.getPermission().getId()
                                        )
                                        .permissionCode(
                                                rp.getPermission()
                                                        .getPermissionCode()
                                        )
                                        .module(
                                                rp.getPermission().getModule()
                                        )
                                        .action(
                                                rp.getPermission().getAction()
                                        )
                                        .description(
                                                rp.getPermission()
                                                        .getDescription()
                                        )
                                        .build()
                        )
                        .toList();

        return ResponseEntity.ok(permissions);
    }
}
