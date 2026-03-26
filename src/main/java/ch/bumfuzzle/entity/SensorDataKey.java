package ch.bumfuzzle.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class SensorDataKey {

    private Instant timestamp;

    private UUID deviceId;
}
