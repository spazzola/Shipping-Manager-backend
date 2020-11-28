package shippingmanager.utility.plate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shippingmanager.utility.driver.Driver;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlateDto {

    private Long id;
    private String plateNumber;
    private Driver driver;

}
