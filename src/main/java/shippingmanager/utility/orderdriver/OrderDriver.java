package shippingmanager.utility.orderdriver;

import lombok.*;
import shippingmanager.order.Order;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_drivers")
public class OrderDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_driver_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_fk")
    private Order order;

    private String name;
    private String surname;
    private String firstPlate;
    private String secondPlate;
    private String firstPhoneNumber;
    private String secondPhoneNumber;
}