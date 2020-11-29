package shippingmanager.company;


import lombok.*;
import shippingmanager.utility.address.Address;
import shippingmanager.utility.bankaccount.BankAccount;
import shippingmanager.utility.phonenumber.PhoneNumber;

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

    private boolean isContractor;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "address_fk")
    private Address address;

    @OneToMany(mappedBy = "company", cascade=CascadeType.ALL)
    private List<PhoneNumber> phoneNumbers;

    @OneToMany(mappedBy = "company", cascade=CascadeType.ALL)
    private List<BankAccount> bankAccounts;

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", nip='" + nip + '\'' +
                ", regon='" + regon + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", phoneNumbers=" + phoneNumbers +
                ", bankAccounts=" + bankAccounts +
                '}';
    }
}
