package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shippingmanager.company.Company;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Invoice {

    private Long id;
    private Long invoiceNumber;
    private LocalDate issuedDate;
    private LocalDate paymentDate;
    private BigDecimal valueWithTax;
    private BigDecimal valueWithoutTax;
    private Company issuedBy;
    private Company receivedBy;
    private boolean isPaid;

}
