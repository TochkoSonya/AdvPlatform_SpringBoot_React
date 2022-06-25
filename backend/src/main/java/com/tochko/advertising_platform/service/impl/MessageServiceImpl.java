package com.tochko.advertising_platform.service.impl;

import com.tochko.advertising_platform.model.Message;
import com.tochko.advertising_platform.repository.MessageRepository;
import com.tochko.advertising_platform.service.MessageService;
import com.tochko.advertising_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllUserMessages(Long userId) {
        String username = userService.get(userId).get().getUsername();
        return messageRepository.findMessagesByUserId(username);
    }

    @Transactional
    public void removeChat(String otherUserUsername, String currentUserUsername) {
        messageRepository.deleteMessagesByReceiverAndSender(otherUserUsername, currentUserUsername);
    }
}
