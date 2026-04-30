package ch.bumfuzzle.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeviceCreateDto {
    private String name;
    private String description;
}
