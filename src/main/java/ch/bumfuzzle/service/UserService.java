package ch.bumfuzzle.service;

import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.kafka.UserProducer;
import ch.bumfuzzle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProducer userProducer;

    public void createUserIfNotExists(Jwt jwt) {
        if (jwt == null) return;

        String keycloakId = jwt.getSubject();
        if (userRepository.findByKeycloakId(keycloakId).isEmpty()) {
            User user = userRepository.save(new User(jwt.getSubject()));
            userProducer.send(user);
        }
    }
}
