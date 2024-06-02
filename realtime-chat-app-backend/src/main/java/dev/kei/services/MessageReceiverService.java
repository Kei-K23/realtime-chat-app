package dev.kei.services;

import dev.kei.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiverService {
    private static final Logger log = LoggerFactory.getLogger(MessageReceiverService.class);
    private final SimpMessageSendingOperations messageTemplate;
   private final SimpUserRegistry userRegistry;

   public MessageReceiverService(SimpMessageSendingOperations messageTemplate, SimpUserRegistry userRegistry) {
      this.messageTemplate = messageTemplate;
      this.userRegistry = userRegistry;
   }

   @KafkaListener(topics = "messaging", groupId = "chat")
    public void receiveMessage(Message chatMessage) {
       log.info("Received message from kafka: {}", chatMessage);
       for (SimpUser user : userRegistry.getUsers()) {
           for (SimpSession session : user.getSessions()) {
               if (session.getId().equals(chatMessage.getSessionId())) {
                   messageTemplate.convertAndSendToUser(session.getId(), "/topic/public",chatMessage);
               }
           }
       }
   }
}
