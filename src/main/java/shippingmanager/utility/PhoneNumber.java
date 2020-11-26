package shippingmanager.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shippingmanager.company.Company;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PhoneNumber {

    private Long id;
    private String type;
    private String number;
    private Company company;
    private Driver driver;

}
