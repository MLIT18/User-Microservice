package com.insightzz.user.repository;

import com.insightzz.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //Optional<User> findByUserName(String userName);

    Optional<User> findByUserEmail(String userEmail);

    boolean existsByEmployeId(String employeId);

    boolean existsByUserEmail(String userEmail);

    boolean existsByEmployeIdAndUserEmail(String employeId, String userEmail);

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.role
        ORDER BY LOWER(u.firstName) ASC, LOWER(u.lastName) ASC
        """)
    List<User> findAllWithRole();

    List<User> findByRole_RoleNameAndIsActiveTrue(
            String roleName
    );

    @Query("""
    SELECT u
    FROM User u
    WHERE
        u.isActive = True
        AND (
            LOWER(u.employeId) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.department) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.userEmail) LIKE LOWER(CONCAT('%', :search, '%'))
        )
    ORDER BY
        LOWER(CONCAT(u.firstName, ' ', u.lastName)) ASC
    """)
    List<User> searchUsers(
            @Param("search") String search
    );

    @Query("""
    SELECT u
    FROM User u
    WHERE
        u.isActive = True
        AND (
            LOWER(u.employeId) LIKE LOWER(CONCAT(:query, '%'))
            OR LOWER(u.department) LIKE LOWER(CONCAT(:query, '%'))
            OR LOWER(u.firstName) LIKE LOWER(CONCAT(:query, '%'))
            OR LOWER(u.lastName) LIKE LOWER(CONCAT(:query, '%'))
            OR LOWER(u.userEmail) LIKE LOWER(CONCAT(:query, '%'))
        )
    ORDER BY
        LOWER(CONCAT(u.firstName, ' ', u.lastName)) ASC
    """)
    List<User> findSuggestions(
            @Param("query") String query,
            Pageable pageable
    );
}
