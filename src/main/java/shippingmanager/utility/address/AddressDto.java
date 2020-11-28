package shippingmanager.utility.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;
    private String postalCode;
    private String city;
    private String town;
    private String street;
    private String houseNumber;

}
