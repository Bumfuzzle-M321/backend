package ch.bumfuzzle.kafka;

import ch.bumfuzzle.entity.SensorData;
import ch.bumfuzzle.entity.SensorDataKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SensorDataConsumer {

  @KafkaListener(
      topics = "${app.kafka.topic:test-topic}",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(final List<ConsumerRecord<SensorDataKey, SensorData>> payload) {
    try {
      log.info("Consumed {} record of  {}", payload.size(), SensorDataKey.class.getTypeName());
    } catch (final Exception e) {
      log.error("Failed to process Message", e);
    }
  }
}
