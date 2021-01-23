package shippingmanager.utility.product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public List<Product> calculateValues(List<Product> products) {
        for (Product product : products) {
            BigDecimal valueWithoutTax = product.getPriceWithoutTax().multiply(product.getQuantity());
            product.setValueWithoutTax(valueWithoutTax);
            BigDecimal valueWithTax = calculateValueWithTax(valueWithoutTax, product.getTax());
            product.setValueWithTax(valueWithTax);
            product.setTaxValue(valueWithTax.subtract(valueWithoutTax));
        }
        return products;
    }

    private BigDecimal calculateValueWithTax(BigDecimal valueWithoutTax, BigDecimal tax) {
        return valueWithoutTax.add(valueWithoutTax.multiply(tax));
    }

}
