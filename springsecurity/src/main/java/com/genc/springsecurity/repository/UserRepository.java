package com.genc.springsecurity.repository;

import com.genc.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);
}