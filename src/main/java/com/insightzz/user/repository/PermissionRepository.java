package com.insightzz.user.repository;

import com.insightzz.user.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository
        extends JpaRepository<Permission, Integer> {

    List<Permission> findByIsActiveTrue();
}