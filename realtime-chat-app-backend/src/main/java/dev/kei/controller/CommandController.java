package dev.kei.controller;

import dev.kei.entity.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandController {
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    public CommandController(KafkaTemplate<String, Message> kafkaTemplate, SimpMessageSendingOperations messagingTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public void send(@RequestBody Message message) {
        kafkaTemplate.send("messaging", message);
        messagingTemplate.convertAndSend("/topic/public", message);
    }
}