package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    
    // Retrieve all messages posted by a specific user
    List<Message> findByPostedBy(Integer postedBy);

    // Delete message given messageId
    @Query("DELETE FROM Message m where m.id = :messageId")
    int deleteByIdCustom(Integer messageId);

    @Query("UPDATE Message m SET m.messageText = :newText WHERE m.id = :messageId")
    int updateMessageTextById(Integer messageId, String newText);


    

}
