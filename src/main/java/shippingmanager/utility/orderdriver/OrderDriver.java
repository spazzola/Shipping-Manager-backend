package shippingmanager.utility.orderdriver;

import lombok.*;
import shippingmanager.order.Order;
import shippingmanager.utility.driver.Driver;
import shippingmanager.utility.plate.Plate;

import javax.persistence.*;
import java.util.List;

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
//
//    @ManyToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name = "driver_fk")
//    private Driver driver;

    private String name;
    private String surname;
    private String firstPlate;
    private String secondPlate;
    private String firstPhoneNumber;
    private String secondPhoneNumber;
}