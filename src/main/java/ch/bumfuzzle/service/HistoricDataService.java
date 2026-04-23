package ch.bumfuzzle.service;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.SensorData;
import ch.bumfuzzle.repository.SensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricDataService {
    private final SensorDataRepository sensorDataRepository;

    public List<SensorData> getHistoricData(final Device device, final Instant startTime) {
        if (device == null) return List.of();
        return sensorDataRepository.findByDeviceAndTimestampGreaterThanEqual(device, startTime);
    }
}
