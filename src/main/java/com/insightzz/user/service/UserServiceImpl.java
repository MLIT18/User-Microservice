package com.insightzz.user.service;

import com.insightzz.user.dto.UserCreateRequest;
import com.insightzz.user.dto.UserResponse;
import com.insightzz.user.dto.UserUpdateRequest;
import com.insightzz.user.entity.User;
import com.insightzz.user.exception.DuplicateUserException;
import com.insightzz.user.exception.UserNotFoundException;
import com.insightzz.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // =========================================================
    // CREATE USER
    // =========================================================

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        // =========================================================
        // USERNAME DUPLICATE CHECK
        // =========================================================

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new DuplicateUserException(
                    "Username already exists"
            );
        }


        // =========================================================
        // EMAIL DUPLICATE CHECK
        // =========================================================

        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new DuplicateUserException(
                    "Email already exists"
            );
        }


        // =========================================================
        // KEEP PLAIN PASSWORD ONLY IN MEMORY
        // =========================================================

        String plainPassword = request.getPassword();


        // =========================================================
        // CREATE USER
        // =========================================================

        User user = User.builder()
                .userName(request.getUserName())

                // BCrypt password for DB
                .password(
                        passwordEncoder.encode(
                                plainPassword
                        )
                )

                .userEmail(request.getUserEmail())
                .userMobNo(request.getUserMobNo())
                .userDesignation(request.getUserDesignation())
                .userDoj(request.getUserDoj())
                .userDol(request.getUserDol())
                .userRole(request.getUserRole())
                .isActive(true)
                .build();


        // =========================================================
        // SAVE
        // =========================================================

        User savedUser =
                userRepository.save(user);


        // =========================================================
        // RESPONSE
        // =========================================================

        return UserResponse.builder()
                .userId(savedUser.getUserId())
                .userRole(savedUser.getUserRole())
                .userName(savedUser.getUserName())
                // Return only the password entered during this request
                .password(plainPassword)
                .userEmail(savedUser.getUserEmail())
                .userMobNo(savedUser.getUserMobNo())
                .userDesignation(savedUser.getUserDesignation())
                .userDoj(savedUser.getUserDoj())
                .userDol(savedUser.getUserDol())
                .isActive(savedUser.getIsActive())
                .createDatetime(savedUser.getCreateDatetime())
                .updateDatetime(savedUser.getUpdateDatetime())



                .build();
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        return mapToResponse(user);
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    @Override
    public UserResponse updateUser(
            Long userId,
            UserUpdateRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );


        // =========================================================
        // USERNAME
        // =========================================================

        if (request.getUserName() != null
                && !request.getUserName()
                .equals(user.getUserName())) {

            if (userRepository.existsByUserName(
                    request.getUserName()
            )) {
                throw new DuplicateUserException(
                        "Username already exists"
                );
            }

            user.setUserName(request.getUserName());
        }


        // =========================================================
        // EMAIL
        // =========================================================

        if (request.getUserEmail() != null
                && !request.getUserEmail()
                .equals(user.getUserEmail())) {

            if (userRepository.existsByUserEmail(
                    request.getUserEmail()
            )) {
                throw new DuplicateUserException(
                        "Email already exists"
                );
            }

            user.setUserEmail(request.getUserEmail());
        }


        // =========================================================
        // OTHER FIELDS
        // =========================================================

        if (request.getUserMobNo() != null) {
            user.setUserMobNo(
                    request.getUserMobNo()
            );
        }

        if (request.getUserDesignation() != null) {
            user.setUserDesignation(
                    request.getUserDesignation()
            );
        }

        if (request.getUserDoj() != null) {
            user.setUserDoj(
                    request.getUserDoj()
            );
        }

        if (request.getUserDol() != null) {
            user.setUserDol(
                    request.getUserDol()
            );
        }

        if (request.getUserRole() != null) {
            user.setUserRole(
                    request.getUserRole()
            );
        }

        if (request.getIsActive() != null) {
            user.setIsActive(
                    request.getIsActive()
            );
        }


        // =========================================================
        // PASSWORD UPDATE
        // =========================================================

        String plainPassword = request.getPassword();

        if (plainPassword != null &&
                !plainPassword.isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            plainPassword
                    )
            );
        }


        // =========================================================
        // SAVE
        // =========================================================

        User updatedUser =
                userRepository.save(user);


        // =========================================================
        // RESPONSE
        // =========================================================

        UserResponse.UserResponseBuilder response =
                UserResponse.builder()
                        .userId(updatedUser.getUserId())
                        .userRole(updatedUser.getUserRole())
                        .userName(updatedUser.getUserName())
                        .userEmail(updatedUser.getUserEmail())
                        .userMobNo(updatedUser.getUserMobNo())
                        .userDesignation(
                                updatedUser.getUserDesignation()
                        )
                        .userDoj(updatedUser.getUserDoj())
                        .userDol(updatedUser.getUserDol())
                        .isActive(updatedUser.getIsActive())
                        .createDatetime(
                                updatedUser.getCreateDatetime()
                        )
                        .updateDatetime(
                                updatedUser.getUpdateDatetime()
                        );

        // Return plain password ONLY when password was updated
        if (plainPassword != null &&
                !plainPassword.isBlank()) {

            response.password(plainPassword);
        }

        return response.build();
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        userRepository.delete(user);
    }


    // =========================================================
    // ENTITY → RESPONSE DTO
    // =========================================================

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .userId(user.getUserId())
                .userRole(user.getUserRole())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userMobNo(user.getUserMobNo())
                .userDesignation(user.getUserDesignation())
                .userDoj(user.getUserDoj())
                .userDol(user.getUserDol())
                .isActive(user.getIsActive())
                .createDatetime(user.getCreateDatetime())
                .updateDatetime(user.getUpdateDatetime())
                .build();
    }
}
