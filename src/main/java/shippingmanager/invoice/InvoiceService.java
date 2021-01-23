package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.company.Company;
import shippingmanager.company.CompanyDao;
import shippingmanager.order.Order;
import shippingmanager.order.OrderDao;
import shippingmanager.utility.generalnumber.GeneralNumberService;
import shippingmanager.utility.product.Product;
import shippingmanager.utility.product.ProductMapper;
import shippingmanager.utility.product.ProductService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.List;

@AllArgsConstructor
@Service
public class InvoiceService {

    private final OrderDao orderDao;
    private final InvoiceDao invoiceDao;
    private final CompanyDao companyDao;
    private final ProductMapper productMapper;
    private final GeneralNumberService generalNumberService;
    private final ProductService productService;


    public Invoice createInvoice(CreateInvoiceToOrderRequest createInvoiceToOrderRequest) {
        Order order = orderDao.findById(createInvoiceToOrderRequest.getOrderId())
                .orElseThrow(NoSuchElementException::new);

        List<Product> products = Collections.singletonList(Product.builder()
                .productName("Usługa transportowa")
                .measureUnit("szt")
                .quantity(BigDecimal.valueOf(1))
                .tax(BigDecimal.valueOf(0.23))
                .priceWithoutTax(order.getValue())
                .build());

        products = productService.calculateValues(products);

        BigDecimal valueWithoutTax = order.getValue();
        BigDecimal valueWithTax = valueWithoutTax.add(valueWithoutTax.multiply(BigDecimal.valueOf(0.23)));

        Invoice invoice = Invoice.builder()
                .invoiceNumber(order.getOrderNumber())
                .issuedIn(createInvoiceToOrderRequest.getIssuedIn())
                .issuedDate(createInvoiceToOrderRequest.getIssuedDate())
                .currency(order.getCurrency())
                .daysTillPayment(createInvoiceToOrderRequest.getDaysTillPayment())
                .valueWithoutTax(valueWithoutTax)
                .valueWithTax(valueWithTax)
                .order(order)
                .products(products)
                .issuedBy(order.getReceivedBy())
                .receivedBy(order.getGivenBy())
                .isPaid(createInvoiceToOrderRequest.isPaid())
                .paymentMethod(createInvoiceToOrderRequest.getPaymentMethod())
                .paidAmount(createInvoiceToOrderRequest.getPaidAmount())
                .toPay(createInvoiceToOrderRequest.getToPay())
                .build();

        for (Product product : products) {
            product.setInvoice(invoice);
        }

        invoice = calculateInvoiceValues(invoice);

        return invoiceDao.save(invoice);
    }

    public Invoice createInvoice(CreateInvoiceRequest createInvoiceRequest) {
        Company mainCompany = companyDao.findByIsMainCompanyTrue();
        List<Product> products = productMapper.convertFromDto(createInvoiceRequest.getProducts());
        products = productService.calculateValues(products);
        String invoiceNumber = generalNumberService.generateNumber(createInvoiceRequest.getIssuedDate());

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .issuedIn(createInvoiceRequest.getIssuedIn())
                .paymentMethod(createInvoiceRequest.getPaymentMethod())
                .currency(createInvoiceRequest.getCurrency())
                .daysTillPayment(createInvoiceRequest.getDaysTillPayment())
                .issuedDate(createInvoiceRequest.getIssuedDate())
                .issuedBy(mainCompany)
                .receivedBy(createInvoiceRequest.getReceivedBy())
                .products(products)
                .paidAmount(createInvoiceRequest.getPaidAmount())
                .isPaid(createInvoiceRequest.isPaid())
                .toPay(createInvoiceRequest.getToPay())
                .build();

        for (Product product : products) {
            product.setInvoice(invoice);
        }

        invoice = calculateInvoiceValues(invoice);

        return invoiceDao.save(invoice);
    }

    public Invoice calculateInvoiceValues(Invoice invoice) {
        BigDecimal valueWithoutTax = new BigDecimal(0);
        BigDecimal valueWithTax = new BigDecimal(0);

        for (Product product : invoice.getProducts()) {
            valueWithoutTax = valueWithoutTax.add(product.getValueWithoutTax());
            valueWithTax = valueWithTax.add(product.getValueWithTax());
        }

        invoice.setValueWithoutTax(valueWithoutTax);
        invoice.setValueWithTax(valueWithTax);

        return invoice;
    }

}