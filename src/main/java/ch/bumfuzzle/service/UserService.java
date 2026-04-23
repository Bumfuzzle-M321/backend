package ch.bumfuzzle.service;

import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUserIfNotExists(Jwt jwt) {
        if (jwt == null) return;

        String keycloakId = jwt.getSubject();
        if (userRepository.findByKeycloakId(keycloakId).isEmpty()) {
            userRepository.save(new User(jwt.getSubject()));
        }
    }
}
