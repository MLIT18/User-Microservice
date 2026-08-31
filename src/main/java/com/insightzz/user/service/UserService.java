package com.insightzz.user.service;

import com.insightzz.user.dto.UserCreateRequest;
import com.insightzz.user.dto.UserResponse;
import com.insightzz.user.dto.UserSuggestionResponse;
import com.insightzz.user.dto.UserUpdateRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long userId, UserUpdateRequest request);

    void deleteUser(Long userId);

    List<UserResponse> getUsersByRole(String roleName);

    List<UserResponse> searchUsers(String trim);

    List<UserSuggestionResponse> getUserSuggestions(String trim, @Min(value = 1, message = "Limit must be at least 1") @Max(value = 20, message = "Limit cannot exceed 20") int limit);
}
