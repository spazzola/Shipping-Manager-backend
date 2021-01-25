package shippingmanager.utility.taxxinfo;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxInfo {

    private BigDecimal tax;
    private BigDecimal totalTaxValue;
    private BigDecimal totalValueWithTax;
    private BigDecimal totalValueWithoutTax;

}