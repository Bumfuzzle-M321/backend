package ch.bumfuzzle.config;

import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    // TODO: REMOVE LATER
    @Bean
    public CommandLineRunner loadTestUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Only create test user if it doesn't exist
            if (userRepository.findByUsername("admin").isEmpty()) {
                User testUser = new User("admin", passwordEncoder.encode("password"));
                userRepository.save(testUser);
                System.out.println("Test user 'admin' created with password 'password'");
            }
        };
    }
}

