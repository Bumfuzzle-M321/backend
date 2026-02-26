package ch.bumfuzzle.kafka;

import ch.bumfuzzle.entity.SensorData;
import ch.bumfuzzle.repository.SensorDataRepository;
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
  private final SensorDataRepository sensorDataRepository;

  public KafkaConsumer(final ObjectMapper objectMapper, final KafkaWebsocketHandler wsHandler, final SensorDataRepository sensorDataRepository) {
    this.wsHandler = wsHandler;
    this.objectMapper = objectMapper;
    this.sensorDataRepository = sensorDataRepository;
  }

  @KafkaListener(
      topics = "${app.kafka.topic:test-topic}",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(final byte[] payload) {
    try {
      final SensorData message = objectMapper.readValue(payload, SensorData.class);
      log.info("Received object: {}", message);

      sensorDataRepository.save(message);
      wsHandler.broadcast(message);

    } catch (final Exception e) {
      log.warn("Failed to process Message", e);

      wsHandler.broadcastError(e.getMessage());
    }
  }
}
