package com.tochko.advertising_platform.repository;

import com.tochko.advertising_platform.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.receiver= ?1 OR m.sender= ?1")
    List<Message> findMessagesByUserId(String username);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.sender=?1 AND m.receiver=?2 OR m.sender=?2 AND m.receiver=?1")
    void deleteMessagesByReceiverAndSender(String receiverName, String senderName);
}
