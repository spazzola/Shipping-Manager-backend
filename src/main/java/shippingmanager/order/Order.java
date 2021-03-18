package shippingmanager.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import shippingmanager.company.Company;
import shippingmanager.utility.loadinginformation.LoadingInformation;
import shippingmanager.utility.orderdriver.OrderDriver;

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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime paymentDate;

    private BigDecimal value;

    private BigDecimal weight;

    private int daysTillPayment;

    private String issuedIn;

    private String currency;

    private String description;

    private String comment;

    private String orderNumber;

    private String orderType;

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


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", paymentDate=" + paymentDate +
                ", value=" + value +
                ", weight=" + weight +
                ", description='" + description + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderType='" + orderType + '\'' +
                ", isInvoiceCreated=" + isInvoiceCreated +
                ", givenBy=" + givenBy +
                ", receivedBy=" + receivedBy +
                ", drivers=" + orderDrivers +
                ", loadingInformation=" + loadingInformation +
                '}';
    }

}