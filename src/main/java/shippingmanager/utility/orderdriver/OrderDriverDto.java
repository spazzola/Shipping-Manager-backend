package shippingmanager.utility.orderdriver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shippingmanager.order.Order;
import shippingmanager.utility.driver.Driver;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDriverDto {

    private Long id;
    private Order order;
    private Driver driver;

}
