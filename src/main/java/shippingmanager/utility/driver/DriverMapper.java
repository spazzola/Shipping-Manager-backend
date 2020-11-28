package shippingmanager.utility.driver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.utility.orderdriver.OrderDriverDto;
import shippingmanager.utility.orderdriver.OrderDriverMapper;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.phonenumber.PhoneNumberMapper;
import shippingmanager.utility.plate.PlateDto;
import shippingmanager.utility.plate.PlateMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class DriverMapper {

    private PhoneNumberMapper phoneNumberMapper;
    private OrderDriverMapper orderDriverMapper;
    private PlateMapper plateMapper;

    public DriverDto toDto(Driver driver) {
        final List<OrderDriverDto> orderDriverDto = orderDriverMapper.toDto(driver.getOrderDrivers());
        final List<PhoneNumberDto> phoneNumbersDto = phoneNumberMapper.toDto(driver.getPhoneNumbers());
        final List<PlateDto> platesDto = plateMapper.toDto(driver.getPlates());

        return DriverDto.builder()
                .id(driver.getId())
                .name(driver.getName())
                .surname(driver.getSurname())
                .orderDrivers(orderDriverDto)
                .plates(platesDto)
                .phoneNumbers(phoneNumbersDto)
                .build();
    }

    public List<DriverDto> toDto(List<Driver> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
