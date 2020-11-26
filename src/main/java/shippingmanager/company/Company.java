package shippingmanager.company;


import jdk.internal.jline.internal.Nullable;
import lombok.*;
import shippingmanager.utility.Address;
import shippingmanager.utility.BankAccount;
import shippingmanager.utility.PhoneNumber;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    private String companyName;

    private String nip;

    private String regon;

    private String email;

    @OneToOne
    @JoinColumn(name = "address_fk")
    private Address address;

    @OneToMany(mappedBy = "company")
    private List<PhoneNumber> phoneNumbers;

    @OneToMany(mappedBy = "company")
    private List<BankAccount> bankAccounts;

}
