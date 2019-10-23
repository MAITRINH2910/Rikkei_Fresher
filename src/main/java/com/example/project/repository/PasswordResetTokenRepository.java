package com.example.project.repository;

import com.example.project.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 1. By extending JpaRepository we get a bunch of generic CRUD methods to create, update, delete, and find contacts.
 * 2. It allows Spring to scan the classpath for this interface and create a Spring bean for it.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

}