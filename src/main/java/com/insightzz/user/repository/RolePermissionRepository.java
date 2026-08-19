package com.insightzz.user.repository;

import com.insightzz.user.entity.RolePermission;
import com.insightzz.user.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository
        extends JpaRepository<
        RolePermission,
        RolePermissionId> {

    List<RolePermission> findByRoleId(Integer roleId);
}