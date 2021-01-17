package shippingmanager.utility.loadinginformation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class LoadingInformationMapper {


    public LoadingInformationDto toDto(LoadingInformation loadingInformation) {

        return LoadingInformationDto.builder()
                .id(loadingInformation.getId())
                .loadingPlace(loadingInformation.getLoadingPlace())
                .unloadingPlace(loadingInformation.getUnloadingPlace())
                .minLoadingDate(loadingInformation.getMinLoadingDate())
                .maxLoadingDate(loadingInformation.getMaxLoadingDate())
                .minUnloadingDate(loadingInformation.getMinUnloadingDate())
                .maxUnloadingDate(loadingInformation.getMaxUnloadingDate())
                .build();
    }

    public LoadingInformation fromDto(LoadingInformationDto  loadingInformation) {

        return LoadingInformation.builder()
                .loadingPlace(loadingInformation.getLoadingPlace())
                .unloadingPlace(loadingInformation.getUnloadingPlace())
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
