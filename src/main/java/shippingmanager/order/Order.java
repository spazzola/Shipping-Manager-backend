package shippingmanager.order;

import lombok.*;
import org.joda.time.DateTime;
import shippingmanager.company.Company;
import shippingmanager.invoice.Invoice;
import shippingmanager.utility.Driver;
import shippingmanager.utility.LoadingInformation;
import shippingmanager.utility.OrderDrivers;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;
    private DateTime createdDate;
    private DateTime paymentDate;
    private BigDecimal value;
    private BigDecimal weight;
    private String description;
    private String orderNumber;
    private boolean isInvoiceCreated;
    private Company givenBy;
    private Company receivedBy;
    private List<OrderDrivers> orderDrivers;
    private Invoice invoice;
    private LoadingInformation loadingInformation;

}
