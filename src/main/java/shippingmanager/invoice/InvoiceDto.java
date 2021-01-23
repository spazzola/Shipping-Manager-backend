package shippingmanager.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String paymentMethod;
    private String currency;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime issuedDate;

    private int daysTillPayment;
    private BigDecimal valueWithTax;
    private BigDecimal valueWithoutTax;
    private BigDecimal paidAmount;
    private BigDecimal toPay;
    private OrderDto order;
    private CompanyDto issuedBy;
    private CompanyDto receivedBy;
    private boolean isPaid;

}