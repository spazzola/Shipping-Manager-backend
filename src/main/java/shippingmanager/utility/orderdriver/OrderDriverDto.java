package shippingmanager.utility.orderdriver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDriverDto {

    private Long id;
    private String name;
    private String surname;
    private String firstPlate;
    private String secondPlate;
    private String firstPhoneNumber;
    private String secondPhoneNumber;

}
