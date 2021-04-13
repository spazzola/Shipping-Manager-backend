package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.company.CompanyDto;
import shippingmanager.company.CompanyMapper;
import shippingmanager.order.OrderDto;
import shippingmanager.order.OrderMapper;
import shippingmanager.utility.product.Product;
import shippingmanager.utility.product.ProductDto;
import shippingmanager.utility.product.ProductMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class InvoiceMapper {

    private final OrderMapper orderMapper;
    private final CompanyMapper companyMapper;
    private final ProductMapper productMapper;

    public InvoiceDto toDto(Invoice invoice) {
        OrderDto orderDto = assignOrderIfExist(invoice);
        CompanyDto issuedBy = companyMapper.toDto(invoice.getIssuedBy());
        CompanyDto receivedBy = companyMapper.toDto(invoice.getReceivedBy());
        List<ProductDto> products = productMapper.toDto(invoice.getProducts());

        return InvoiceDto.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .issuedIn(invoice.getIssuedIn())
                .paymentMethod(invoice.getPaymentMethod())
                .currency(invoice.getCurrency())
                .issuedDate(invoice.getIssuedDate())
                .daysTillPayment(invoice.getDaysTillPayment())
                .valueWithTax(invoice.getValueWithTax())
                .valueWithoutTax(invoice.getValueWithoutTax())
                .order(orderDto)
                .issuedBy(issuedBy)
                .receivedBy(receivedBy)
                .isPaid(invoice.isPaid())
                .paidAmount(invoice.getPaidAmount())
                .amountToPay(invoice.getAmountToPay())
                .products(products)
                .build();
    }

    public List<InvoiceDto> toDto(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private OrderDto assignOrderIfExist(Invoice invoice) {
        if (invoice.getOrder() != null) {
            return  orderMapper.toDto(invoice.getOrder());
        } else {
            return null;
        }
    }

}