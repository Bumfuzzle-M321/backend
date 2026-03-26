package ch.bumfuzzle.service;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.SensorData;
import ch.bumfuzzle.entity.SensorDataKey;
import ch.bumfuzzle.repository.DeviceRepository;
import ch.bumfuzzle.repository.SensorDataRepository;
import ch.bumfuzzle.websocket.KafkaWebsocketHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorDataService {

    private final KafkaWebsocketHandler wsHandler;
    private final SensorDataRepository sensorDataRepository;
    private final DeviceRepository deviceRepository;

    public void consumeAll(List<ConsumerRecord<SensorDataKey, SensorData>> records) {
        records.forEach(this::handleRecord);
    }

    private void handleRecord(ConsumerRecord<SensorDataKey, SensorData> record) {
        SensorData sensorData = record.value();
        Device device = deviceRepository.getReferenceById(record.key().getDeviceId());
        sensorData.setDevice(device);

        sensorDataRepository.save(sensorData);
        wsHandler.broadcast(sensorData);
    }
}
