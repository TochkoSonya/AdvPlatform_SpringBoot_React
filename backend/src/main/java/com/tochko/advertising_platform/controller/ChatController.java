package com.tochko.advertising_platform.controller;

import com.tochko.advertising_platform.model.Message;
import com.tochko.advertising_platform.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(MessageService messageService,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/sendPrivateMessage")
    public void sendPrivateMessage(@Payload Message chatMessage) {
        messageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver().trim(), "/reply", chatMessage);
    }

    @MessageMapping("/addPrivateUser")
    @SendTo("/queue/reply")
    public Message addPrivateUser(@Payload Message chatMessage,
                                  SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("private-username", chatMessage.getSender());
        return chatMessage;
    }
}
