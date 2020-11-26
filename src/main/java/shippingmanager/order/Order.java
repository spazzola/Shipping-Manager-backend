package shippingmanager.order;

import lombok.*;
import org.joda.time.DateTime;
import shippingmanager.company.Company;
import shippingmanager.utility.LoadingInformation;
import shippingmanager.utility.OrderDriver;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    private DateTime createdDate;

    private DateTime paymentDate;

    private BigDecimal value;

    private BigDecimal weight;

    private String description;

    private String orderNumber;

    private boolean isInvoiceCreated;

    @ManyToOne
    @JoinColumn(name = "given_by_fk")
    private Company givenBy;

    @ManyToOne
    @JoinColumn(name = "received_by_fk")
    private Company receivedBy;

    @OneToMany(mappedBy = "order")
    private List<OrderDriver> orderDrivers;

    @OneToOne
    @JoinColumn(name = "loading_information_fk")
    private LoadingInformation loadingInformation;

}
