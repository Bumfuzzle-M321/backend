package ch.bumfuzzle.repository;

import ch.bumfuzzle.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
