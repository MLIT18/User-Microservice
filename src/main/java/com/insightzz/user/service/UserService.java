package com.insightzz.user.service;

import com.insightzz.user.dto.UserCreateRequest;
import com.insightzz.user.dto.UserResponse;
import com.insightzz.user.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long userId, UserUpdateRequest request);

    void deleteUser(Long userId);
}
