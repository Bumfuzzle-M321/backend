package ch.bumfuzzle.controller;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.DeviceCreateDto;
import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.repository.DeviceRepository;
import ch.bumfuzzle.repository.UserRepository;
import ch.bumfuzzle.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Device>> findAll(@AuthenticationPrincipal Jwt jwt) {
        User user = userRepository.findByKeycloakId(jwt.getSubject()).orElse(null);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(deviceService.findAll(user));
    }

    @PostMapping("/new")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody DeviceCreateDto deviceCreateDto, @AuthenticationPrincipal Jwt jwt) {
        User user = userRepository.findByKeycloakId(jwt.getSubject()).orElse(null);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(deviceService.createDevice(deviceCreateDto, user));
    }
}
