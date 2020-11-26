package shippingmanager.utility;

import lombok.*;
import shippingmanager.order.Order;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDrivers {

    private Long id;
    private Order order;
    private Driver driver;

}
