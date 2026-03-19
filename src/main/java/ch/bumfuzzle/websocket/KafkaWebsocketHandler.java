package ch.bumfuzzle.websocket;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.SensorData;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class KafkaWebsocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions =
            ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(@NonNull final WebSocketSession session) {
        log.info("WS Session with ID: {} started", session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(
            @NonNull final WebSocketSession session,
            @NonNull final CloseStatus status
    ) {
        log.info("WS Session with ID: {} removed", session.getId());
        sessions.remove(session);
    }

    public void broadcast(final SensorData payload) {
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(
                            new TextMessage(payload.toString())
                    );
                }
            } catch (IOException e) {
                // ignore
            }
        });
    }

    public void broadcastError(final String errorMessage) {
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(
                            new TextMessage(errorMessage)
                    );
                }
            } catch (IOException e) {
                // ignore
            }
        });
    }
}
