package shippingmanager.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Plate {

    private Long id;
    private String plateNumber;
    private Driver driver;

}
