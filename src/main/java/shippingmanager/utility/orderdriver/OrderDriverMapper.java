package shippingmanager.utility.orderdriver;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDriverMapper {

    public OrderDriverDto toDto(OrderDriver orderDriver) {
        return OrderDriverDto.builder()
                .id(orderDriver.getId())
                .driver(orderDriver.getDriver())
                .order(orderDriver.getOrder())
                .build();
    }

    public List<OrderDriverDto> toDto(List<OrderDriver> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
