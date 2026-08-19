package com.insightzz.user.repository;

import com.insightzz.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserEmail(String userEmail);

    boolean existsByUserName(String userName);

    boolean existsByUserEmail(String userEmail);
}
