package shippingmanager.utility.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public List<Product> getProductWithTax23(List<Product> products) {
        List<Product> resultList = new ArrayList<>();
        for (Product product : products) {
            if (product.getTax().equals(BigDecimal.valueOf(0.23))) {
                resultList.add(product);
            }

        }
        return resultList;
    }

    public List<Product> getProductWithTax08(List<Product> products) {
        List<Product> resultList = new ArrayList<>();
        for (Product product : products) {
            if (product.getTax().equals(BigDecimal.valueOf(0.08))) {
                resultList.add(product);
            }

        }
        return resultList;
    }

    public List<Product> getProductWithTax05(List<Product> products) {
        List<Product> resultList = new ArrayList<>();
        for (Product product : products) {
            if (product.getTax().equals(BigDecimal.valueOf(0.05))) {
                resultList.add(product);
            }

        }
        return resultList;
    }

    public void calculateValues(List<Product> products) {
        for (Product product : products) {
            BigDecimal valueWithoutTax = product.getPriceWithoutTax().multiply(product.getQuantity());
            product.setValueWithoutTax(valueWithoutTax);
            BigDecimal valueWithTax = calculateValueWithTax(valueWithoutTax, product.getTax());
            product.setValueWithTax(valueWithTax);
            product.setTaxValue(valueWithTax.subtract(valueWithoutTax));
        }
    }

    private BigDecimal calculateValueWithTax(BigDecimal valueWithoutTax, BigDecimal tax) {
        return valueWithoutTax.add(valueWithoutTax.multiply(tax));
    }

}