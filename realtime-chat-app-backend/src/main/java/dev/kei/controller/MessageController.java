package dev.kei.controller;

import dev.kei.entity.Message;
import dev.kei.services.MessageSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final MessageSenderService messageSenderService;
    private final SimpMessageSendingOperations messageTemplate;

    public MessageController(MessageSenderService messageSenderService, SimpMessageSendingOperations messageTemplate) {
        this.messageSenderService = messageSenderService;
        this.messageTemplate = messageTemplate;
    }

    @MessageMapping("/chat.send-message")
    public void sendMessage(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        chatMessage.setSessionId(headerAccessor.getSessionId());
        messageSenderService.sendMessage("messaging", chatMessage);
        log.info("Sending message to /topic/public: {}", chatMessage);
        messageTemplate.convertAndSend("/topic/public", chatMessage);
        log.info("Message sent to /topic/public: {}", chatMessage);
    }

    @MessageMapping("/chat.add-user")
    public Message addUser(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        }

        return chatMessage;
    }
}
