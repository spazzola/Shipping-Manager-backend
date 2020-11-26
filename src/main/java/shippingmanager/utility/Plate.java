package shippingmanager.utility;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plates")
public class Plate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plate_id")
    private Long id;

    private String plateNumber;

    @ManyToOne
    @JoinColumn(name = "driver_fk")
    private Driver driver;

}
