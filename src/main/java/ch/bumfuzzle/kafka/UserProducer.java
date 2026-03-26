package ch.bumfuzzle.kafka;

import ch.bumfuzzle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserProducer {

    @Value("${app.kafka.topic.user}")
    public static String USER_TOPIC;

    private final KafkaTemplate<Instant, User> kafkaTemplate;

    public void send(User user) {
        kafkaTemplate.send(USER_TOPIC, Instant.now(), user);
    }
}
