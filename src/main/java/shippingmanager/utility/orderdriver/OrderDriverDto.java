package shippingmanager.utility.orderdriver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shippingmanager.utility.driver.DriverDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDriverDto {

    private Long id;
    private DriverDto driver;

}
