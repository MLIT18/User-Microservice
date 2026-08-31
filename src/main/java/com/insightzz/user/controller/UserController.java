package com.insightzz.user.controller;

import com.insightzz.user.dto.UserCreateRequest;
import com.insightzz.user.dto.UserResponse;
import com.insightzz.user.dto.UserSuggestionResponse;
import com.insightzz.user.dto.UserUpdateRequest;
import com.insightzz.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    // =========================================================
    // CREATE USER
    // =========================================================
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================
//    @PreAuthorize("hasAuthority('USER_READ')")
//    @GetMapping
//    public ResponseEntity<List<UserResponse>> getAllUsers() {
//
//        List<UserResponse> users = userService.getAllUsers();
//
//        return ResponseEntity.ok(users);
//    }


    // =========================================================
    // GET USER BY ID
    // =========================================================
    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId) {
        //log.info("USER CONTROLLER HIT");
        UserResponse response =
                userService.getUserById(userId);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // UPDATE USER
    // =========================================================
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest request) {

        UserResponse response =
                userService.updateUser(userId, request);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE USER
    // =========================================================
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-role")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserResponse>> getUsersByRole(
            @RequestParam String roleName) {

        return ResponseEntity.ok(
                userService.getUsersByRole(roleName)
        );
    }

    // =========================================================
    // SEARCH / FILTER / GET ALL USERS
    // =========================================================

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(required = false) String search) {

        List<UserResponse> users;

        if (search == null || search.isBlank()) {
            users = userService.getAllUsers();
        } else {
            users = userService.searchUsers(search.trim());
        }

        return ResponseEntity.ok(users);
    }


    // =========================================================
    // USER SUGGESTIONS / AUTOCOMPLETE
    // =========================================================

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/suggestions")
    public ResponseEntity<List<UserSuggestionResponse>> getUserSuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Limit must be at least 1")
            @Max(value = 20, message = "Limit cannot exceed 20")
            int limit) {

        return ResponseEntity.ok(
                userService.getUserSuggestions(q.trim(), limit)
        );
    }

}
