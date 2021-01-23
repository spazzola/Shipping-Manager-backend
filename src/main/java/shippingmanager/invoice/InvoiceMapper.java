package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.company.CompanyDto;
import shippingmanager.company.CompanyMapper;
import shippingmanager.order.OrderDto;
import shippingmanager.order.OrderMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class InvoiceMapper {

    private final OrderMapper orderMapper;
    private final CompanyMapper companyMapper;

    public InvoiceDto toDto(Invoice invoice) {
        OrderDto orderDto = orderMapper.toDto(invoice.getOrder());
        CompanyDto issuedBy = companyMapper.toDto(invoice.getIssuedBy());
        CompanyDto receivedBy = companyMapper.toDto(invoice.getReceivedBy());

        return InvoiceDto.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .issuedIn(invoice.getIssuedIn())
                .paymentMethod(invoice.getPaymentMethod())
                .issuedDate(invoice.getIssuedDate())
                .paymentDate(invoice.getPaymentDate())
                .valueWithTax(invoice.getValueWithTax())
                .valueWithoutTax(invoice.getValueWithoutTax())
                .order(orderDto)
                .issuedBy(issuedBy)
                .receivedBy(receivedBy)
                .isPaid(invoice.isPaid())
                .paidAmount(invoice.getPaidAmount())
                .build();
    }

    public List<InvoiceDto> toDto(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}