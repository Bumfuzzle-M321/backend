package ch.bumfuzzle.kafka;

import ch.bumfuzzle.entity.TestEntity;
import ch.bumfuzzle.repository.TestRepository;
import ch.bumfuzzle.websocket.KafkaWebsocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class KafkaConsumer {

  private final ObjectMapper objectMapper;

  private final KafkaWebsocketHandler wsHandler;
  private final TestRepository testRepository;

  public KafkaConsumer(final ObjectMapper objectMapper, final KafkaWebsocketHandler wsHandler, final TestRepository testRepository) {
    this.wsHandler = wsHandler;
    this.objectMapper = objectMapper;
    this.testRepository = testRepository;
  }

  @KafkaListener(
      topics = "${app.kafka.topic:test-topic}",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(final byte[] payload) {
    try {
      final TestEntity message = objectMapper.readValue(payload, TestEntity.class);
      log.info("Received object: {}", message);

      testRepository.save(message);
      wsHandler.broadcast(message);

    } catch (final Exception e) {
      log.warn("Failed to process Message", e);

      wsHandler.broadcastError(e.getMessage());
    }
  }
}
