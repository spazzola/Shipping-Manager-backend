package shippingmanager.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shippingmanager.company.CompanyDto;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.orderdriver.OrderDriverDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime paymentDate;

    private BigDecimal value;
    private BigDecimal weight;
    private int daysTillPayment;
    private String issuedIn;
    private String currency;
    private String description;
    private String comment;
    private String orderNumber;
    private String orderType;
    private boolean isInvoiceCreated;
    private CompanyDto givenBy;
    private CompanyDto receivedBy;
    private List<OrderDriverDto> orderDrivers;
    private LoadingInformationDto loadingInformation;

}
