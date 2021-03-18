package shippingmanager.utility.plate;

import org.springframework.stereotype.Component;
import shippingmanager.utility.driver.DriverDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlateMapper {


    public PlateDto toDto(Plate plate) {
        return PlateDto.builder()
                .id(plate.getId())
                .plateNumber(plate.getPlateNumber())
                .build();
    }

    public List<PlateDto> toDto(List<Plate> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Plate> fromDto(List<PlateDto> platesDto) {
        List<Plate> plates = new ArrayList<>();
        for (PlateDto plateDto : platesDto) {
            plates.add(Plate.builder()
                    .id(plateDto.getId())
                    .plateNumber(plateDto.getPlateNumber())
                    .build());
        }

        return plates;
    }



}
