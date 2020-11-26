package shippingmanager.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import shippingmanager.company.Company;
import shippingmanager.invoice.Invoice;
import shippingmanager.utility.Driver;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Order {

    private Long id;
    private Company givenBy;
    private Company receivedBy;
    private Company loadingPlace;
    private Company unloadingPlace;
    private DateTime createdDate;
    private DateTime minLoadingDate;
    private DateTime maxLoadingDate;
    private DateTime minUnloadingDate;
    private DateTime maxUnloadingDate;
    private DateTime paymentDate;
    private BigDecimal value;
    private String description;
    private boolean isInvoiceCreated;
    private String orderNumber;
    private BigDecimal weight;
    private Driver driver;
    private Invoice invoice;

}
