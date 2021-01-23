package shippingmanager.utility.generalNumber;

import lombok.*;
import org.springframework.stereotype.Service;
import shippingmanager.order.Order;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "general_numbers")
public class GeneralNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number_id")
    private Long id;
    private BigDecimal number;

}