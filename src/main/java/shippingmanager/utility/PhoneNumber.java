package shippingmanager.utility;

import lombok.*;
import shippingmanager.company.Company;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_number_id")
    private Long id;

    private String type;

    private String number;

    @ManyToOne
    @JoinColumn(name = "company_fk")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "driver_fk")
    private Driver driver;

}
