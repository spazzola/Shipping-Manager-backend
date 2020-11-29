package shippingmanager.order;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import shippingmanager.company.CompanyDto;
import shippingmanager.utility.CustomDateTimeDeserializer;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.orderdriver.OrderDriverDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime createdDate;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime paymentDate;

    private BigDecimal value;
    private BigDecimal weight;
    private String description;
    private String orderNumber;
    private boolean isInvoiceCreated;
    private CompanyDto givenBy;
    private CompanyDto receivedBy;
    private List<OrderDriverDto> orderDrivers;
    private LoadingInformationDto loadingInformation;

}
