package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.company.Company;
import shippingmanager.company.CompanyDao;
import shippingmanager.order.Order;
import shippingmanager.order.OrderDao;
import shippingmanager.utility.generalnumber.GeneralNumberService;
import shippingmanager.utility.product.Product;
import shippingmanager.utility.product.ProductMapper;
import shippingmanager.utility.product.ProductService;

import java.math.BigDecimal;
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


    @Transactional
    public Invoice createInvoice(CreateInvoiceToOrderRequest createInvoiceToOrderRequest) {
        Order order = orderDao.findById(createInvoiceToOrderRequest.getOrderId())
                .orElseThrow(NoSuchElementException::new);
        Company mainCompany = companyDao.findByIsMainCompanyTrue();

        List<Product> products = Collections.singletonList(Product.builder()
                .productName("Usługa transportowa")
                .measureUnit("szt")
                .quantity(BigDecimal.valueOf(1))
                .tax(BigDecimal.valueOf(0.23))
                .priceWithoutTax(order.getValue())
                .build());

        productService.calculateValues(products);

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
                .issuedBy(mainCompany)
                .receivedBy(order.getGivenBy())
                .isPaid(createInvoiceToOrderRequest.isPaid())
                .paymentMethod(createInvoiceToOrderRequest.getPaymentMethod())
                .paidAmount(createInvoiceToOrderRequest.getPaidAmount())
                .amountToPay(createInvoiceToOrderRequest.getToPay())
                .build();

        setProductsToInvoice(products, invoice);

        calculateAndSetInvoiceValues(invoice);
        calculateAndSetAmountToPay(invoice);

        return invoiceDao.save(invoice);
    }

    @Transactional
    public Invoice createInvoice(CreateInvoiceRequest createInvoiceRequest) {
        Company mainCompany = companyDao.findByIsMainCompanyTrue();
        List<Product> products = productMapper.convertFromDto(createInvoiceRequest.getProducts());
        productService.calculateValues(products);
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
                .amountToPay(createInvoiceRequest.getAmountToPay())
                .build();

        setProductsToInvoice(products, invoice);

        calculateAndSetInvoiceValues(invoice);
        calculateAndSetAmountToPay(invoice);

        return invoiceDao.save(invoice);
    }

    private void setProductsToInvoice(List<Product> products, Invoice invoice) {
        for (Product product : products) {
            product.setInvoice(invoice);
        }
    }

    private void calculateAndSetInvoiceValues(Invoice invoice) {
        BigDecimal valueWithoutTax = new BigDecimal(0);
        BigDecimal valueWithTax = new BigDecimal(0);

        for (Product product : invoice.getProducts()) {
            valueWithoutTax = valueWithoutTax.add(product.getValueWithoutTax());
            valueWithTax = valueWithTax.add(product.getValueWithTax());
        }
        invoice.setValueWithoutTax(valueWithoutTax);
        invoice.setValueWithTax(valueWithTax);
    }

    private void calculateAndSetAmountToPay(Invoice invoice) {
        BigDecimal paidAmount = invoice.getPaidAmount();
        BigDecimal amountToPay = invoice.getValueWithTax().subtract(paidAmount);
        invoice.setAmountToPay(amountToPay);
    }

}