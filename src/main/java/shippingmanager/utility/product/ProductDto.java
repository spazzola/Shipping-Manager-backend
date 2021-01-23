package shippingmanager.utility.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String productName;
    private String measureUnit;
    private BigDecimal quantity;
    private BigDecimal taxValue;
    private BigDecimal valueWithTax;
    private BigDecimal valueWithoutTax;

}