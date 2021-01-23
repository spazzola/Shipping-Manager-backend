package shippingmanager.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateInvoiceToOrderRequest {

    private Long orderId;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime issuedDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime paymentDate;

    private String issuedIn;

    private String paymentMethod;

    private BigDecimal paidAmount;

    private boolean isPaid;


    @Override
    public String toString() {
        return "CreateInvoiceToOrderRequest{" +
                "orderId=" + orderId +
                ", issuedDate=" + issuedDate +
                ", paymentDate=" + paymentDate +
                ", isPaid=" + isPaid +
                '}';
    }

}