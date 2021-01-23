package shippingmanager.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import shippingmanager.company.Company;
import shippingmanager.utility.product.Product;
import shippingmanager.utility.product.ProductDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateInvoiceRequest {

    private String issuedIn;
    private String paymentMethod;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime issuedDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime paymentDate;

    private Company receivedBy;
    private List<ProductDto> products;
    private BigDecimal paidAmount;
    private boolean isPaid;

}