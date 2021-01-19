package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import shippingmanager.company.CompanyDto;
import shippingmanager.order.OrderDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class InvoiceDto {

    private Long id;
    private String issuedIn;
    private String invoiceNumber;
    private String productName;
    private String paymentMethod;
    private LocalDateTime issuedDate;
    private LocalDateTime paymentDate;
    private BigDecimal valueWithTax;
    private BigDecimal valueWithoutTax;
    private OrderDto order;
    private CompanyDto issuedBy;
    private CompanyDto receivedBy;
    private boolean isPaid;

}