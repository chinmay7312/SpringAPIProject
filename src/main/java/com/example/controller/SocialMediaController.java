package com.example.controller;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
 public class SocialMediaController {
    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    // Handler for Registering Accounts
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        if(account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Blank Username");
        }

        if(account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password Needs To Be At Least 4 Characters");
        }

        // Check if username already exists
        if (accountService.usernameExists(account.getUsername())) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        // Save the new account
        try {
            Account savedAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(savedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Handler for Authenticating Logins
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Optional<Account> authenticatedUser = accountService.authenticateUser(account.getUsername(), account.getPassword());
        
        if (authenticatedUser.isPresent()) {
            return ResponseEntity.ok(authenticatedUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
        }
    }

    // Handler for Creating Messages
    @PostMapping("/messages")
    public ResponseEntity<?> createMessages(@RequestBody Message message) {
        
        // Validate messageText: must not be blank or over 255 characters
        if(message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Blank messageText field");
        }

        if(message.getMessageText().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("messageText too long");
        }
        
        // Validate postedBy: must refer to an existing user
        if (!accountService.existsById(message.getPostedBy())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID: No such user exists.");
        }

        // Try Creating the Message
        try {
            Message savedMessage = messageService.createMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured while saving the message");
        }
    }

    // Handler For Getting All Messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // Handler For Retrieving a Message given the messageId
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    // Handler For Deleting a Message given the messageId
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessageById(messageId);

        if(rowsDeleted > 0) {
            return ResponseEntity.ok(rowsDeleted);
        }
        return ResponseEntity.ok().build();
    }

    // Handler For Updating a Message given the messageId
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String,String> updateRequest) {
        String newMessageText = updateRequest.get("messageText");

        // Validate messageText
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message text cannot be blank.");
        }

        if (newMessageText.length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message text cannot exceed 255 characters.");
        }

        int rowsUpdated = messageService.updateMessageText(messageId, newMessageText);
        
        if (rowsUpdated > 0) {
            return ResponseEntity.ok(rowsUpdated); // Return 1 if the message was updated
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message ID does not exist.");
        }
    }

    // Handler For Retrieving all Messages Written by a Particular User
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);
    }
}
