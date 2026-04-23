package ch.bumfuzzle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "device")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Device {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, columnDefinition = "TEXT", length = 15)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @ManyToOne()
  private User user;

  @OneToMany()
  private List<SensorData> sensorData;
}
