package dev.kei.listener;

import dev.kei.entity.Message;
import dev.kei.entity.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {
    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final SimpMessageSendingOperations messageTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new WebSocket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");

        if (username != null) {
            log.info("User Disconnected: {}", username);
            Message chatMessage = new Message();
            chatMessage.setType(MessageType.DISCONNECT);
            chatMessage.setSender(username);
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
