package shippingmanager.order;

import lombok.*;
import org.joda.time.DateTime;
import shippingmanager.company.Company;
import shippingmanager.invoice.Invoice;
import shippingmanager.utility.Driver;
import shippingmanager.utility.LoadingInformation;

import java.math.BigDecimal;

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
    private Driver driver;
    private Invoice invoice;
    private LoadingInformation loadingInformation;

}
