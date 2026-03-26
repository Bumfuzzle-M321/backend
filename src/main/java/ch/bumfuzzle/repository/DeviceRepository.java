package ch.bumfuzzle.repository;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<List<Device>> findAllByUser(User user);
}
