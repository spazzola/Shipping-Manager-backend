package shippingmanager.company;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shippingmanager.utility.Address;
import shippingmanager.utility.BankAccount;
import shippingmanager.utility.PhoneNumber;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Company {

    private Long id;
    private String companyName;
    private String nip;
    private String regon;
    private String email;
    private Address address;
    private List<PhoneNumber> phoneNumbers;
    private List<BankAccount> bankAccounts;

}
