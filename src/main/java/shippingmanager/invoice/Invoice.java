package shippingmanager.invoice;

import lombok.*;
import shippingmanager.company.Company;
import shippingmanager.order.Order;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String invoiceNumber;

    private LocalDateTime issuedDate;

    private LocalDateTime paymentDate;

    private BigDecimal valueWithTax;

    private BigDecimal valueWithoutTax;

    @OneToOne
    @JoinColumn(name = "order_fk")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "issued_by_fk")
    private Company issuedBy;

    @ManyToOne
    @JoinColumn(name = "received_by_fk")
    private Company receivedBy;

    private boolean isPaid;

}