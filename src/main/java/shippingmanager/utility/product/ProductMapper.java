package shippingmanager.utility.product;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .measureUnit(product.getMeasureUnit())
                .quantity(product.getQuantity())
                .taxValue(product.getTax())
                .valueWithTax(product.getValueWithTax())
                .valueWithoutTax(product.getValueWithoutTax())
                .priceWithoutTax(product.getPriceWithoutTax())
                .build();
    }

    public List<ProductDto> toDto(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Product fromDto(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .id(productDto.getId())
                .productName(productDto.getProductName())
                .measureUnit(productDto.getMeasureUnit())
                .quantity(productDto.getQuantity())
                .tax(productDto.getTax())
                .taxValue(productDto.getTaxValue())
                .priceWithoutTax(productDto.getPriceWithoutTax())
                .valueWithTax(productDto.getValueWithTax())
                .valueWithoutTax(productDto.getValueWithoutTax())
                .build();
    }

    public List<Product> fromDto(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

}