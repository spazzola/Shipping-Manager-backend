package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import shippingmanager.company.Company;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Invoice {

    private Long id;
    private String invoiceNumber;
    private DateTime issuedDate;
    private DateTime paymentDate;
    private BigDecimal valueWithTax;
    private BigDecimal valueWithoutTax;
    private Company issuedBy;
    private Company receivedBy;
    private boolean isPaid;

}
