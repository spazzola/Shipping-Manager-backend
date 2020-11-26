package shippingmanager.utility;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    private String name;

    private String surname;

    @OneToMany(mappedBy = "driver")
    private List<OrderDriver> orderDriver;

    @OneToMany(mappedBy = "driver")
    private List<Plate> plates;

    @OneToMany(mappedBy = "driver")
    private List<PhoneNumber> phoneNumbers;

}
