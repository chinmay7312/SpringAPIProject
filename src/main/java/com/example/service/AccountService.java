package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    AccountRepository accountRepository;
    
    /**
     * Registers a new account in the database.
     * @param account The account to be registered.
     * @return The persisted account.
     */
    public Account registerAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Checks if an account exists by username.
     * @param username The username to search for.
     * @return An Optional containing the found account or empty if not found.
     */
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * Checks if a username already exists (efficient lookup).
     */
    public boolean usernameExists(String username) {
        return accountRepository.existsByUsername(username);
    }

    /**
     * Authenticates a user by verifying username and password.
     * @return Optional<Account> if credentials are valid, else empty.
     */
    public Optional<Account> authenticateUser(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username,password);
    }

    /**
     * Checks if an account with the given ID exists in the database.
     * @param accountId The ID of the account to check.
     * @return true if the account exists, false otherwise.
     */
    public boolean existsById(Integer accountId) {
        return accountRepository.existsById(accountId);
    }
}
