package shippingmanager.utility.taxxinfo;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.utility.product.Product;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Service
public class TaxInfoService {


    public TaxInfo createTaxInfo(List<Product> products) {
        if (products.size() > 0) {
            TaxInfo taxInfo = new TaxInfo();
            taxInfo.setTotalValueWithoutTax(BigDecimal.ZERO);
            taxInfo.setTotalValueWithTax(BigDecimal.ZERO);
            taxInfo.setTotalTaxValue(BigDecimal.ZERO);

            for (Product product : products) {
                BigDecimal valueWithoutTax = taxInfo.getTotalValueWithoutTax();
                valueWithoutTax = valueWithoutTax.add(product.getValueWithoutTax());
                taxInfo.setTotalValueWithoutTax(valueWithoutTax);
                taxInfo.setTax(product.getTax());

                BigDecimal valueWithTax = taxInfo.getTotalValueWithTax();
                valueWithTax = valueWithTax.add(product.getValueWithTax());
                taxInfo.setTotalValueWithTax(valueWithTax);

                BigDecimal totalTaxValue = taxInfo.getTotalTaxValue();
                totalTaxValue = totalTaxValue.add(product.getTaxValue());
                taxInfo.setTotalTaxValue(totalTaxValue);
            }
            return taxInfo;
        } else {
            return null;
        }
    }

}