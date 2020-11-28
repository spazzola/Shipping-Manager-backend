package shippingmanager.utility.phonenumber;

import org.springframework.stereotype.Component;
import shippingmanager.company.Company;
import shippingmanager.company.CompanyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoneNumberMapper {

    public PhoneNumberDto toDto(PhoneNumber phoneNumber) {
        return PhoneNumberDto.builder()
                .id(phoneNumber.getId())
                .number(phoneNumber.getNumber())
                .type(phoneNumber.getType())
                .build();
    }

    public List<PhoneNumber> fromDto(CompanyDto companyDto, Company company) {
        final List<PhoneNumberDto> phoneNumbersDto = companyDto.getPhoneNumbers();
        final List<PhoneNumber> phoneNumbers = new ArrayList<>();

        for (PhoneNumberDto phoneNumberDto : phoneNumbersDto) {
            phoneNumbers.add(PhoneNumber.builder()
                    .id(phoneNumberDto.getId())
                    .type(phoneNumberDto.getType())
                    .number(phoneNumberDto.getNumber())
                    .company(company)
                    .build());
        }
        return phoneNumbers;
    }


    public List<PhoneNumberDto> toDto(List<PhoneNumber> phoneNumbers) {
        return phoneNumbers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
