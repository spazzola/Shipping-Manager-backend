package shippingmanager.utility.driver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.utility.phonenumber.PhoneNumber;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.phonenumber.PhoneNumberMapper;
import shippingmanager.utility.plate.Plate;
import shippingmanager.utility.plate.PlateDto;
import shippingmanager.utility.plate.PlateMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class DriverMapper {

    private PhoneNumberMapper phoneNumberMapper;
    private PlateMapper plateMapper;

    public DriverDto toDto(Driver driver) {
        final List<PhoneNumberDto> phoneNumbersDto = phoneNumberMapper.toDto(driver.getPhoneNumbers());
        final List<PlateDto> platesDto = plateMapper.toDto(driver.getPlates());

        return DriverDto.builder()
                .id(driver.getId())
                .name(driver.getName())
                .surname(driver.getSurname())
                .plates(platesDto)
                .phoneNumbers(phoneNumbersDto)
                .build();
    }

    public List<DriverDto> toDto(List<Driver> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Driver> fromDto(List<DriverDto> driversDto) {
        List<Driver> drivers = new ArrayList<>();
        for (DriverDto driverDto : driversDto) {
            drivers.add(fromDto(driverDto));
        }

        return drivers;
    }

    public Driver fromDto(DriverDto driverDto) {
        List<Plate> plates = plateMapper.fromDto(driverDto.getPlates());
        List<PhoneNumber> phoneNumbers = phoneNumberMapper.fromDto(driverDto);

        return Driver.builder()
                .name(driverDto.getName())
                .surname(driverDto.getSurname())
                .plates(plates)
                .phoneNumbers(phoneNumbers)
                .build();
    }

}

