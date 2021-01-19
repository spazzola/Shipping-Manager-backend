package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.order.Order;
import shippingmanager.order.OrderDao;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class InvoiceService {

    private final OrderDao orderDao;
    private final InvoiceDao invoiceDao;

    public Invoice createInvoiceToOrder(CreateInvoiceToOrderRequest createInvoiceToOrderRequest) {
        Order order = orderDao.findById(createInvoiceToOrderRequest.getOrderId())
                .orElseThrow(NoSuchElementException::new);

        BigDecimal valueWithoutTax = order.getValue();
        BigDecimal valueWithTax = valueWithoutTax.add(valueWithoutTax.multiply(BigDecimal.valueOf(0.23)));

        Invoice invoice = Invoice.builder()
                .invoiceNumber(order.getOrderNumber())
                .issuedIn(createInvoiceToOrderRequest.getIssuedIn())
                .issuedDate(createInvoiceToOrderRequest.getIssuedDate())
                .paymentDate(createInvoiceToOrderRequest.getPaymentDate())
                .valueWithoutTax(valueWithoutTax)
                .valueWithTax(valueWithTax)
                .order(order)
                .issuedBy(order.getReceivedBy())
                .receivedBy(order.getGivenBy())
                .isPaid(createInvoiceToOrderRequest.isPaid())
                .build();


        return invoiceDao.save(invoice);
    }

}