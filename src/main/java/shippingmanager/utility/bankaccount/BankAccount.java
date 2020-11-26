package shippingmanager.utility.bankaccount;

import lombok.*;
import shippingmanager.company.Company;

import javax.persistence.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private Long id;

    private String accountName;

    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "company_fk")
    private Company company;

}
