package shippingmanager.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import shippingmanager.company.Company;
import shippingmanager.utility.loadinginformation.LoadingInformation;
import shippingmanager.utility.orderdriver.OrderDriver;

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

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
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

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderDriver> orderDrivers;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "loading_information_fk")
    private LoadingInformation loadingInformation;

}
