package ch.bumfuzzle.repository;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findByDeviceAndTimestampGreaterThanEqual(Device device, Instant timestamp);
}
