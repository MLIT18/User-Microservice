package com.insightzz.user.controller;

import com.insightzz.user.dto.RoleResponse;
import com.insightzz.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

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
}
