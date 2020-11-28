package shippingmanager.utility.plate;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlateMapper {

    public PlateDto toDto(Plate plate) {
        return PlateDto.builder()
                .id(plate.getId())
                .plateNumber(plate.getPlateNumber())
                .driver(plate.getDriver())
                .build();
    }

    public List<PlateDto> toDto(List<Plate> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
