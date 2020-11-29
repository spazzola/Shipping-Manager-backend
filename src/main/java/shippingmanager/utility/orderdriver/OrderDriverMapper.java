package shippingmanager.utility.orderdriver;

import org.springframework.stereotype.Component;
import shippingmanager.utility.driver.DriverDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDriverMapper {

    public OrderDriverDto toDto(OrderDriver orderDriver) {
        DriverDto driverDto = DriverDto.builder()
                .name(orderDriver.getDriver().getName())
                .surname(orderDriver.getDriver().getSurname())
                .build();

        return OrderDriverDto.builder()
                .id(orderDriver.getId())
                .driver(driverDto)
                .build();
    }

    public List<OrderDriverDto> toDto(List<OrderDriver> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
