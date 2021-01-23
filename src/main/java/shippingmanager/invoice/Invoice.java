package shippingmanager.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import shippingmanager.company.Company;
import shippingmanager.order.Order;
import shippingmanager.utility.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    protected String issuedIn;

    private String invoiceNumber;

    protected String paymentMethod;

    private String currency;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime issuedDate;

    private int daysTillPayment;

    private BigDecimal valueWithTax;

    private BigDecimal valueWithoutTax;

    private BigDecimal paidAmount;

    private BigDecimal toPay;

    @OneToOne
    @JoinColumn(name = "order_fk")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "issued_by_fk")
    private Company issuedBy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "received_by_fk")
    private Company receivedBy;

    protected boolean isPaid;

    @JsonIgnore
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Product> products;

}