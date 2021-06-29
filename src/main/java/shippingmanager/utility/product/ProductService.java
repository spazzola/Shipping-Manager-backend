package shippingmanager.utility.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductDao productDao;

    public List<Product> getProductWithTax23(List<Product> products) {
        List<Product> resultList = new ArrayList<>();
        for (Product product : products) {
            if (product.getTax().compareTo(BigDecimal.valueOf(23)) == 0) {
                resultList.add(product);
            }

        }
        return resultList;
    }

    public List<Product> getProductWithTax08(List<Product> products) {
        List<Product> resultList = new ArrayList<>();
        for (Product product : products) {
            if (product.getTax().compareTo(BigDecimal.valueOf(8)) == 0) {
                resultList.add(product);
            }

        }
        return resultList;
    }

    public List<Product> getProductWithTax05(List<Product> products) {
        List<Product> resultList = new ArrayList<>();
        for (Product product : products) {
            if (product.getTax().compareTo(BigDecimal.valueOf(5)) == 0) {
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

    @Transactional
    public void deleteProducts(List<ProductDto> productsDto) throws Exception {
        for (ProductDto productDto : productsDto) {
            Product product = productDao.findById(productDto.getId())
                    .orElseThrow(Exception::new);
            productDao.delete(product);
        }
    }

    private BigDecimal calculateValueWithTax(BigDecimal valueWithoutTax, BigDecimal tax) {
        BigDecimal convertedTaxValue = tax.divide(BigDecimal.valueOf(100));
        return valueWithoutTax.add(valueWithoutTax.multiply(convertedTaxValue));
    }

}