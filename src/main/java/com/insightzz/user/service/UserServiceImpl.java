package com.insightzz.user.service;

import com.insightzz.user.dto.UserCreateRequest;
import com.insightzz.user.dto.UserResponse;
import com.insightzz.user.dto.UserSuggestionResponse;
import com.insightzz.user.dto.UserUpdateRequest;
import com.insightzz.user.entity.Role;
import com.insightzz.user.entity.User;
import com.insightzz.user.exception.DuplicateUserException;
import com.insightzz.user.exception.UserNotFoundException;
import com.insightzz.user.repository.RoleRepository;
import com.insightzz.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final RoleRepository roleRepository;

    // =========================================================
    // CREATE USER
    // =========================================================

    @Override
    public UserResponse createUser(
            UserCreateRequest request) {

        // =========================================================
// USERNAME + EMAIL DUPLICATE CHECK
// =========================================================

        boolean employeIdExists = userRepository.existsByEmployeId(request.getEmployeId());
        boolean emailExists = userRepository.existsByUserEmail(request.getUserEmail());

        if (employeIdExists && emailExists) {

            // Confirm it's the SAME user having both matching username and email
            boolean sameUserHasBoth = userRepository.existsByEmployeIdAndUserEmail(
                    request.getEmployeId(),
                    request.getUserEmail()
            );

            if (sameUserHasBoth) {
                throw new DuplicateUserException(
                        "Employe already exists"
                );
            }
        }

// otherwise, proceed with insert

        // =========================================================
        // FIND ROLE
        // =========================================================

        Role role = roleRepository
                .findById(request.getRoleId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid role selected"
                        )
                );

        // =========================================================
        // CHECK ROLE ACTIVE
        // =========================================================

        if (!Boolean.TRUE.equals(role.getIsActive())) {

            throw new IllegalArgumentException(
                    "Selected role is inactive"
            );
        }

        // =========================================================
        // PASSWORD
        // =========================================================

        String plainPassword = request.getPassword();

        // =========================================================
        // CREATE USER
        // =========================================================

        User user = User.builder()
                .employeId(request.getEmployeId())
                .department(request.getDepartment())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
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
                .role(role)
                .isActive(true)
                .build();

        User savedUser =
                userRepository.save(user);

        // =========================================================
        // RESPONSE
        // =========================================================

        return UserResponse.builder()
                .userId(savedUser.getUserId())
                .userRole(role.getRoleName())
                .roleId(role.getId())
               // .userName(savedUser.getUserName())
                .employeId(savedUser.getEmployeId())
                .department(savedUser.getDepartment())
                .password(plainPassword)
//                .firstName(savedUser.getFirstName())
//                .lastName(savedUser.getLastName())
                .fullName(
                        savedUser.getFirstName() + " " + savedUser.getLastName()
                )
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

        return userRepository.findAllWithRole()
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

        // =========================================================
        // FIND USER
        // =========================================================

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );


        // =========================================================
        // EmployeID
        // =========================================================

        if (request.getEmployeId() != null
                && !request.getEmployeId()
                .equals(user.getEmployeId())) {

            if (userRepository.existsByEmployeId(
                    request.getEmployeId()
            )) {

                throw new DuplicateUserException(
                        "EmployeId already exists"
                );
            }

            user.setEmployeId(
                    request.getEmployeId()
            );
        }







        // =========================================================
        // MOBILE
        // =========================================================

        if (request.getUserMobNo() != null) {

            user.setUserMobNo(
                    request.getUserMobNo()
            );
        }


        // =========================================================
        // DESIGNATION
        // =========================================================

        if (request.getUserDesignation() != null) {

            user.setUserDesignation(
                    request.getUserDesignation()
            );
        }


        // =========================================================
        // DOL
        // =========================================================

        if (request.getUserDol() != null) {

            user.setUserDol(
                    request.getUserDol()
            );
        }





        // =========================================================
        // ACTIVE / INACTIVE
        // =========================================================

        if (request.getIsActive() != null) {

            user.setIsActive(
                    request.getIsActive()
            );
        }


        // =========================================================
        // PASSWORD UPDATE
        // =========================================================

        String plainPassword =
                request.getPassword();

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
                        .userId(
                                updatedUser.getUserId()
                        )
                        .roleId(
                                updatedUser.getRole().getId()
                        )
                        .userRole(
                                updatedUser.getRole().getRoleName()
                        )
                        .employeId(updatedUser.getEmployeId())

                        .userMobNo(
                                updatedUser.getUserMobNo()
                        )
                        .userDesignation(
                                updatedUser.getUserDesignation()
                        )

                        .userDol(
                                updatedUser.getUserDol()
                        )
                        .isActive(
                                updatedUser.getIsActive()
                        )
                        .createDatetime(
                                updatedUser.getCreateDatetime()
                        )
                        .updateDatetime(
                                updatedUser.getUpdateDatetime()
                        );


        // =========================================================
        // RETURN NEW PASSWORD ONLY
        // =========================================================

        if (plainPassword != null &&
                !plainPassword.isBlank()) {

            response.password(
                    plainPassword
            );
        }


        return response.build();
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        user.setIsActive(false);

        userRepository.save(user);
    }


    // =========================================================
    // ENTITY → RESPONSE DTO
    // =========================================================

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .userId(
                        user.getUserId()
                )
                .roleId(
                        user.getRole().getId()
                )
                .userRole(
                        user.getRole().getRoleName()
                )

                .password(null)
                .fullName(
                        user.getFirstName() + " " + user.getLastName()
                )
                .employeId(user.getEmployeId())
                .department(user.getDepartment())
                .userEmail(
                        user.getUserEmail()
                )
                .userMobNo(
                        user.getUserMobNo()
                )
                .userDesignation(
                        user.getUserDesignation()
                )
                .userDoj(
                        user.getUserDoj()
                )
                .userDol(
                        user.getUserDol()
                )
                .isActive(
                        user.getIsActive()
                )
                .createDatetime(
                        user.getCreateDatetime()
                )
                .updateDatetime(
                        user.getUpdateDatetime()
                )
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(
            String roleName) {

        return userRepository
                .findByRole_RoleNameAndIsActiveTrue(
                        roleName
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String search) {

        if (search == null || search.isBlank()) {
            return getAllUsers();
        }

        return userRepository
                .searchUsers(search.trim())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSuggestionResponse> getUserSuggestions(
            String query,
            int limit) {

        if (query == null || query.isBlank()) {
            return List.of();
        }

        Pageable pageable =
                PageRequest.of(
                        0,
                        limit,
                        Sort.by(
                                Sort.Direction.ASC,
                                "firstName"
                        )
                );

        return userRepository
                .findSuggestions(query.trim(), pageable)
                .stream()
                .map(this::mapToSuggestion)
                .toList();
    }

    private UserSuggestionResponse mapToSuggestion(User user) {

        return UserSuggestionResponse.builder()
                .userId(user.getUserId())
                .employeId(user.getEmployeId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userEmail(user.getUserEmail())
                .department(user.getDepartment())
                .build();
    }
}
