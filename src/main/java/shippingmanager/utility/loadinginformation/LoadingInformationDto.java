package shippingmanager.utility.loadinginformation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import shippingmanager.company.Company;
import shippingmanager.utility.CustomDateTimeDeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadingInformationDto {

    private Long id;
    private Company loadingPlace;
    private Company unLoadingPlace;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime minLoadingDate;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime maxLoadingDate;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime minUnloadingDate;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime maxUnloadingDate;

}
