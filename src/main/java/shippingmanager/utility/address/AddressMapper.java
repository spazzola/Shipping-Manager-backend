package shippingmanager.utility.address;

import org.springframework.stereotype.Component;
import shippingmanager.company.CompanyDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressMapper {

    public AddressDto toDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .houseNumber(address.getHouseNumber())
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .town(address.getTown())
                .build();
    }

    public List<AddressDto> toDto(List<Address> addresses) {
        return addresses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Address fromDto(CompanyDto companyDto) {
        final AddressDto addressDto = companyDto.getAddress();
        return Address.builder()
                .id(addressDto.getId())
                .city(addressDto.getCity())
                .houseNumber(addressDto.getHouseNumber())
                .postalCode(addressDto.getPostalCode())
                .street(addressDto.getStreet())
                .town(addressDto.getTown())
                .build();
    }

}
