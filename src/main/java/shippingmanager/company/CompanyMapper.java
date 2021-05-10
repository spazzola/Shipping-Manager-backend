package shippingmanager.company;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.utility.address.Address;
import shippingmanager.utility.address.AddressDto;
import shippingmanager.utility.address.AddressMapper;
import shippingmanager.utility.bankaccount.BankAccount;
import shippingmanager.utility.bankaccount.BankAccountDto;
import shippingmanager.utility.bankaccount.BankAccountMapper;
import shippingmanager.utility.phonenumber.PhoneNumber;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.phonenumber.PhoneNumberMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CompanyMapper {

    private AddressMapper addressMapper;
    private BankAccountMapper bankAccountMapper;
    private PhoneNumberMapper phoneNumberMapper;


    public CompanyDto toDto(Company company) {
        final AddressDto addressDto = addressMapper.toDto(company.getAddress());
        final List<BankAccountDto> bankAccountsDto = bankAccountMapper.toDto(company.getBankAccounts());
        final List<PhoneNumberDto> phoneNumbersDto = phoneNumberMapper.toDto(company.getPhoneNumbers());

        return CompanyDto.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .nip(company.getNip())
                .regon(company.getRegon())
                .email(company.getEmail())
                .address(addressDto)
                .phoneNumbers(phoneNumbersDto)
                .bankAccounts(bankAccountsDto)
                .isMainCompany(company.isMainCompany())
                .build();
    }

    public List<CompanyDto> toDto(List<Company> companies) {
        return companies.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Company fromDto(CompanyDto companyDto) {
        Address address = addressMapper.fromDto(companyDto.getAddress());
        List<BankAccount> bankAccounts = bankAccountMapper.fromDto(companyDto.getBankAccounts());
        List<PhoneNumber> phoneNumbers = phoneNumberMapper.fromDto(companyDto.getPhoneNumbers());

        return Company.builder()
                .id(companyDto.getId())
                .companyName(companyDto.getCompanyName())
                .nip(companyDto.getNip())
                .regon(companyDto.getRegon())
                .email(companyDto.getEmail())
                .address(address)
                .bankAccounts(bankAccounts)
                .phoneNumbers(phoneNumbers)
                .build();
    }

}
