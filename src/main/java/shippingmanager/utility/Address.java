package shippingmanager.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class Address {

    private Long id;
    private String postalCode;
    private String city;
    private String town;
    private String street;
    private String houseNumber;

}
