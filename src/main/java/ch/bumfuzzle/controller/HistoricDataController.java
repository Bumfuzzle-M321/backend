package ch.bumfuzzle.controller;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.SensorData;
import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.repository.DeviceRepository;
import ch.bumfuzzle.repository.UserRepository;
import ch.bumfuzzle.service.HistoricDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/historic")
@RequiredArgsConstructor
public class HistoricDataController {

    private final UserRepository userRepository;
    private final HistoricDataService historicDataService;

    @GetMapping("/{deviceId}/{timestamp}")
    public List<SensorData> findHistoricDataSinceTimestamp(
            @PathVariable final UUID deviceId,
            @PathVariable final Instant timestamp,
            @AuthenticationPrincipal final Jwt jwt
    ) {
        User user = userRepository.findByKeycloakId(jwt.getSubject()).orElse(null);
        if (user == null) return List.of();
        Optional<Device> selectedDevice = user.getDevices().stream()
                .filter(device -> deviceId.equals(device.getId()))
                .findFirst();

        return historicDataService.getHistoricData(selectedDevice.orElse(null), timestamp);
    }
}
