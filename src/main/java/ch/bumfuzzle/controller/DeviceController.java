package ch.bumfuzzle.controller;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.repository.DeviceRepository;
import ch.bumfuzzle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public List<Device> findAll(@AuthenticationPrincipal Jwt jwt) {
        User user = userRepository.findByKeycloakId(jwt.getSubject()).orElse(null);
        if (user == null) {return List.of();}
        return deviceRepository.findAllByUser(user).orElse(List.of());
    }
}
