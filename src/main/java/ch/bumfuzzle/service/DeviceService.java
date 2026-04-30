package ch.bumfuzzle.service;

import ch.bumfuzzle.entity.Device;
import ch.bumfuzzle.entity.DeviceCreateDto;
import ch.bumfuzzle.entity.User;
import ch.bumfuzzle.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public List<Device> findAll(User user) {
       return deviceRepository.findAllByUser(user).orElse(List.of());
    }

    public Device createDevice(DeviceCreateDto deviceCreateDto, User user) {
        Device device = new Device();
        device.setName(deviceCreateDto.getName());
        device.setDescription(deviceCreateDto.getDescription());
        device.setUser(user);
        return deviceRepository.save(device);
    }
}
