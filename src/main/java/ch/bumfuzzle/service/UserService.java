package ch.bumfuzzle.service;

import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUserIfNotExists(Jwt jwt) {
        if (jwt == null) return;

        String keycloakId = jwt.getSubject();
        userRepository.findByKeycloakId(keycloakId)
                .ifPresentOrElse(user -> {
                }, saveNewUser(new User(keycloakId)));
    }

    public Runnable saveNewUser(User user) {
        return new Runnable() {
            @Override
            public void run() {
                userRepository.save(user);
            }
        };
    }
}
