package shippingmanager.utility.driver;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.plate.PlateDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {

    private Long id;
    private String name;
    private String surname;
    private List<PlateDto> plates;
    private List<PhoneNumberDto> phoneNumbers;

}
