package com.tochko.advertising_platform.service;

import com.tochko.advertising_platform.model.Message;

import java.util.List;

public interface MessageService {

    Message save(Message message);

    List<Message> getAllUserMessages(Long userId);

    void removeChat(String otherUserUsername, String currentUserUsername);
}
