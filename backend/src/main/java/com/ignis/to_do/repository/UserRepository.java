package com.ignis.to_do.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ignis.to_do.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNameAndEmail(String name, String email);
}
