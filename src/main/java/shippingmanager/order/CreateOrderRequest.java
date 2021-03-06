package shippingmanager.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import shippingmanager.utility.driver.DriverDto;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime paymentDate;

    private Long id;
    private BigDecimal value;
    private BigDecimal weight;
    private int daysTillPayment;
    private String issuedIn;
    private String currency;
    private String description;
    private String comment;
    private String orderType;
    private Long givenById;
    private Long receivedById;
    private String shipper;
    private List<DriverDto> drivers;
    private LoadingInformationDto loadingInformation;

}
