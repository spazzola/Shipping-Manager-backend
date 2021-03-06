package shippingmanager.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import shippingmanager.company.CompanyDto;
import shippingmanager.utility.product.ProductDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateInvoiceRequest {

    private Long id;
    private String invoiceNumber;
    private String issuedIn;
    private String paymentMethod;
    private String currency;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime issuedDate;

    private int daysTillPayment;
    private CompanyDto receivedBy;
    private List<ProductDto> products;
    private BigDecimal paidAmount;
    private boolean isPaid;

}