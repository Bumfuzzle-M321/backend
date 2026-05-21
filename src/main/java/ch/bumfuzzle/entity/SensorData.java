package ch.bumfuzzle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sensor_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "JSON")
    private String data;

    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne()
    private Device device;

    @Override
    public String toString() {
        return "SensorData{" +
                "id=" + id +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", device=" + device +
                '}';
    }
}
