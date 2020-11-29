package shippingmanager.order;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.joda.time.DateTime;
import shippingmanager.company.CompanyDto;
import shippingmanager.utility.CustomDateTimeDeserializer;
import shippingmanager.utility.driver.DriverDto;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime createdDate;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime paymentDate;

    private BigDecimal value;
    private BigDecimal weight;
    private String description;
    private Long givenById;
    private Long receivedById;
    private List<DriverDto> drivers;
    private LoadingInformationDto loadingInformation;

}
