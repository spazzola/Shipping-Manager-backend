package shippingmanager.utility.orderdriver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.utility.driver.DriverDto;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.phonenumber.PhoneNumberMapper;
import shippingmanager.utility.plate.Plate;
import shippingmanager.utility.plate.PlateDto;
import shippingmanager.utility.plate.PlateMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderDriverMapper {

    private PlateMapper plateMapper;
    private PhoneNumberMapper phoneNumberMapper;

    public OrderDriverDto toDto(OrderDriver orderDriver) {
//        List<PlateDto> platesDto = plateMapper.toDto(orderDriver.getDriver().getPlates());
//        List<PhoneNumberDto> phoneNumbersDto = phoneNumberMapper.toDto(orderDriver.getDriver().getPhoneNumbers());

//        DriverDto driverDto = DriverDto.builder()
//                .id(orderDriver.getDriver().getId())
//                .name(orderDriver.getDriver().getName())
//                .surname(orderDriver.getDriver().getSurname())
//                .plates(platesDto)
//                .phoneNumbers(phoneNumbersDto)
//                .build();

        return OrderDriverDto.builder()
                .id(orderDriver.getId())
                .name(orderDriver.getName())
                .surname(orderDriver.getSurname())
                .firstPlate(orderDriver.getFirstPlate())
                .secondPlate(orderDriver.getSecondPlate())
                .firstPhoneNumber(orderDriver.getFirstPhoneNumber())
                .secondPhoneNumber(orderDriver.getSecondPhoneNumber())
                .build();
    }

    public List<OrderDriverDto> toDto(List<OrderDriver> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<OrderDriver> fromDto(List<OrderDriverDto> orderDriversDto) {
        return orderDriversDto.stream().map(orderDriverDto ->
                OrderDriver.builder()
                        .name(orderDriverDto.getName())
                        .surname(orderDriverDto.getSurname())
                        .firstPhoneNumber(orderDriverDto.getFirstPhoneNumber())
                        .secondPhoneNumber(orderDriverDto.getSecondPhoneNumber())
                        .firstPlate(orderDriverDto.getFirstPlate())
                        .secondPlate(orderDriverDto.getSecondPlate())
                        .build()).collect(Collectors.toList());
    }

}