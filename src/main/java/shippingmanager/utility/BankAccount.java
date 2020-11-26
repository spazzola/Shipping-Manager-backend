package shippingmanager.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class BankAccount {

    private Long id;
    private String accountName;
    private String accountNumber;

}
