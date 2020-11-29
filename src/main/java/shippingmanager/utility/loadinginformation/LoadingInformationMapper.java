package shippingmanager.utility.loadinginformation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.company.CompanyDto;
import shippingmanager.company.CompanyMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class LoadingInformationMapper {

    private CompanyMapper companyMapper;

    public LoadingInformationDto toDto(LoadingInformation loadingInformation) {
        CompanyDto loadingPlace = companyMapper.toDto(loadingInformation.getLoadingPlace());
        CompanyDto unLoadingPlace = companyMapper.toDto(loadingInformation.getUnloadingPlace());

        return LoadingInformationDto.builder()
                .id(loadingInformation.getId())
                .loadingPlace(loadingPlace)
                .unLoadingPlace(unLoadingPlace)
                .minLoadingDate(loadingInformation.getMinLoadingDate())
                .maxLoadingDate(loadingInformation.getMaxLoadingDate())
                .minUnloadingDate(loadingInformation.getMinUnloadingDate())
                .maxUnloadingDate(loadingInformation.getMaxUnloadingDate())
                .build();
    }

    public List<LoadingInformationDto> toDto(List<LoadingInformation> addresses) {
        return addresses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
