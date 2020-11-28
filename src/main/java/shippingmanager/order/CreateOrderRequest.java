package shippingmanager.order;

import java.util.List;
import lombok.Data;
import org.joda.time.DateTime;
import shippingmanager.company.CompanyDto;
import shippingmanager.utility.driver.DriverDto;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    private DateTime createdDate;
    private DateTime paymentDate;
    private BigDecimal value;
    private BigDecimal weight;
    private String description;
    private String orderNumber;
    private CompanyDto givenBy;
    private CompanyDto receivedBy;
    private List<DriverDto> drivers;
    private LoadingInformationDto loadingInformationDto;

}
