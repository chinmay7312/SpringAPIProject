package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    @Autorwired
    private AccountRepository accountRepository;

    /**
     * Creates a new message if valid.
     * @param message The message entity to save.
     * @return The persisted message.
     */
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    /*
     * Returns all messages by every user
     * @return List of messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message by its ID.
     * @param messageId The ID of the message.
     * @return Optional containing the message if found, otherwise empty.
     */
    public Message getMessageById(Integer messageId) {
        Optional<Message> searchMessage = messageRepository.findById(messageId);
        if(searchMessage.isEmpty()) {
            return null;
        }
        Message foundMessage = searchMessage.get();
        return foundMessage;
    }

    /**
     * Deletes a message given the messageId
     * @param messageId The ID of the message.
     * @return number of rows modified
     */
    public int deleteMessageById(Integer messageId) {
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    /*
     * Updates a message given the messageId and messageText
     * @param messageId The ID of the message.
     * @param messageText The new text of the message
     * @return number of rows modified
     */
    public int updateMessageText(Integer messageId, String newMessageText) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return 1; // Message updated successfully
        }
        
        return 0; // Message ID does not exist
    }

    /**
     * Retrieves all messages posted by a particular user.
     * @param accountId The ID of the user.
     * @return A list of messages posted by the user.
     */
    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
