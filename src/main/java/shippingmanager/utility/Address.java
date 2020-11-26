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
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String postalCode;

    private String city;

    private String town;

    private String street;

    private String houseNumber;

    @OneToOne
    private Company company;

}
