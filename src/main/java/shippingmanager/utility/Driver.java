package shippingmanager.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Driver {

    private Long id;
    private String name;
    private String surname;
    private List<Plate> plates;
    private List<PhoneNumber> phoneNumbers;

}
