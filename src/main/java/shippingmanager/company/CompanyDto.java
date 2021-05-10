package shippingmanager.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shippingmanager.utility.address.AddressDto;
import shippingmanager.utility.bankaccount.BankAccountDto;
import shippingmanager.utility.phonenumber.PhoneNumberDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;
    private String companyName;
    private String nip;
    private String regon;
    private String email;
    private AddressDto address;
    private boolean isMainCompany;
    private List<PhoneNumberDto> phoneNumbers;
    private List<BankAccountDto> bankAccounts;

}
