package ch.bumfuzzle.service;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.SensorData;
import ch.bumfuzzle.entity.SensorDataKey;
import ch.bumfuzzle.repository.DeviceRepository;
import ch.bumfuzzle.repository.SensorDataRepository;
import ch.bumfuzzle.websocket.KafkaWebsocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorDataService {

    private final KafkaWebsocketHandler wsHandler;
    private final SensorDataRepository sensorDataRepository;
    private final DeviceRepository deviceRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void consumeAll(List<ConsumerRecord<String, String>> records) {
        records.forEach(this::handleRecord);
    }

    private void handleRecord(ConsumerRecord<String, String> record) {
        SensorDataKey key = parseKey(record.key());
        Device device = deviceRepository.getReferenceById(key.getDeviceId());

        SensorData sensorData = new SensorData();

        try {
            JsonNode dataNode = objectMapper.readTree(record.value());
            sensorData.setData(dataNode);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON payload", e);
        }

        sensorData.setTimestamp(key.getTimestamp());
        sensorData.setDevice(device);

        log.info("Sensordata: {} was read", sensorData);
        sensorDataRepository.save(sensorData);
        wsHandler.broadcast(sensorData);
    }

    private SensorDataKey parseKey(String key) {
        try {
            JsonNode keyJson = objectMapper.readTree(key);
            return new SensorDataKey(
                    Instant.parse(keyJson.required("timestamp").asText()),
                    UUID.fromString(keyJson.required("device_id").asText())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid sensor data Kafka key: " + key, e);
        }
    }
}
