package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    
    // Find an Account by Username
    Optional<Account> findByUsername(String username);

    // Checks if a Username already exists
    boolean existsByUsername(String username);

    // Finds an Account by Username and Password for Authentication
    Optional<Account> findByUsernameAndPassword(String username, String password);



}
