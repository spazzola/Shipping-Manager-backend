package shippingmanager.utility.product;

import lombok.*;
import shippingmanager.invoice.Invoice;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String productName;
    private String measureUnit;
    private BigDecimal quantity;
    private BigDecimal tax;
    private BigDecimal taxValue;
    private BigDecimal priceWithoutTax;
    private BigDecimal valueWithTax;
    private BigDecimal valueWithoutTax;

    @ManyToOne
    @JoinColumn(name = "invoice_fk")
    private Invoice invoice;

}