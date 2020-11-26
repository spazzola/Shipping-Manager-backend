package shippingmanager.utility;

import lombok.*;
import org.joda.time.DateTime;
import shippingmanager.company.Company;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadingInformation {

    private Company loadingPlace;
    private Company unloadingPlace;
    private DateTime minLoadingDate;
    private DateTime maxLoadingDate;
    private DateTime minUnloadingDate;
    private DateTime maxUnloadingDate;

}
