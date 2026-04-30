package ch.bumfuzzle.kafka;

import ch.bumfuzzle.entity.SensorData;
import ch.bumfuzzle.entity.SensorDataKey;
import ch.bumfuzzle.service.SensorDataService;
import ch.bumfuzzle.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SensorDataConsumer {

  private final SensorDataService sensorDataService;

  @KafkaListener(
      topics = "${app.kafka.topic.sensorData:test-topic}",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(final List<ConsumerRecord<SensorDataKey, SensorData>> payload) {
    try {
      log.info("Consumed {} records of  {}", payload.size(), SensorDataKey.class.getTypeName());
      sensorDataService.consumeAll(payload);
    } catch (final Exception e) {
      log.error("Failed to process Message", e);
    }
  }
}
