package com.ignis.to_do.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ignis.to_do.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNameAndEmail(String name, String email);
    boolean existsByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE users u SET u.name = :name, u.email = :email, u.password = :password WHERE u.id = :id")
    void updateUser(
        @Param("id") long id,
        @Param("name") String name,
        @Param("email") String email,
        @Param("password") String password
        );

    @Modifying
    @Query("UPDATE users u SET u.password = :password WHERE u.id = :id")
    void updatePasswordById(
        @Param("id") long id,
        @Param("password") String password
        );
}
