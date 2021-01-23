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

import java.math.BigDecimal;
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


    public Invoice createInvoice(CreateInvoiceToOrderRequest createInvoiceToOrderRequest) {
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
                .paymentMethod(createInvoiceToOrderRequest.getPaymentMethod())
                .paidAmount(createInvoiceToOrderRequest.getPaidAmount())
                .build();

        return invoiceDao.save(invoice);
    }

    public Invoice createInvoice(CreateInvoiceRequest createInvoiceRequest) {
        Company mainCompany = companyDao.findByIsMainCompanyTrue();
        List<Product> products = productMapper.convertFromDto(createInvoiceRequest.getProducts());
        products = setToProductValueWithTax(products);
        String invoiceNumber = generalNumberService.generateNumber(createInvoiceRequest.getIssuedDate());

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .issuedIn(createInvoiceRequest.getIssuedIn())
                .paymentMethod(createInvoiceRequest.getPaymentMethod())
                .paymentDate(createInvoiceRequest.getPaymentDate())
                .issuedDate(createInvoiceRequest.getIssuedDate())
                .issuedBy(mainCompany)
                .receivedBy(createInvoiceRequest.getReceivedBy())
                .products(products)
                .paidAmount(createInvoiceRequest.getPaidAmount())
                .isPaid(createInvoiceRequest.isPaid())
                .build();

        for (Product product : products) {
            product.setInvoice(invoice);
        }

        return invoiceDao.save(invoice);
    }

    private List<Product> setToProductValueWithTax(List<Product> products) {
        for (Product product : products) {
            BigDecimal valueWithTax = calculateValueWithTax(product.getValueWithoutTax(), product.getTaxValue());
            product.setValueWithTax(valueWithTax);
        }

        return products;
    }

    private BigDecimal calculateValueWithTax(BigDecimal valueWithoutTax, BigDecimal taxValue) {
        return valueWithoutTax.add(valueWithoutTax.multiply(taxValue));
    }

}