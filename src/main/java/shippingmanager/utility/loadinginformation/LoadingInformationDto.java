package shippingmanager.utility.loadinginformation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import shippingmanager.company.Company;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadingInformationDto {

    private Long id;
    private Company loadingPlace;
    private Company unLoadingPlace;
    private DateTime minLoadingDate;
    private DateTime maxLoadingDate;
    private DateTime minUnloadingDate;
    private DateTime maxUnloadingDate;
    
}
