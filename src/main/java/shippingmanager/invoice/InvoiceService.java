package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.company.*;
import shippingmanager.order.Order;
import shippingmanager.order.OrderDao;
import shippingmanager.order.UpdateOrderRequest;
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
    private final CompanyMapper companyMapper;
    private final CompanyService companyService;
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
                .tax(BigDecimal.valueOf(23))
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
                .receivedBy(order.getReceivedBy())
                .isPaid(createInvoiceToOrderRequest.isPaid())
                .paymentMethod(createInvoiceToOrderRequest.getPaymentMethod())
                .paidAmount(createInvoiceToOrderRequest.getPaidAmount())
                .amountToPay(createInvoiceToOrderRequest.getToPay())
                .build();

        setProductsToInvoice(products, invoice);

        calculateAndSetInvoiceValues(invoice);
        calculateAndSetAmountToPay(invoice);

        order.setInvoiceCreated(true);
        orderDao.save(order);

        return invoiceDao.save(invoice);
    }

    @Transactional
    public Invoice createInvoice(CreateInvoiceRequest createInvoiceRequest) {
        Company mainCompany = companyDao.findByIsMainCompanyTrue();
        //Company receivedBy = companyMapper.fromDto(createInvoiceRequest.getReceivedBy());
        Company company = companyService.createCompany(createInvoiceRequest.getReceivedBy());

        List<Product> products = productMapper.fromDto(createInvoiceRequest.getProducts());
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
                .receivedBy(company)
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

    @Transactional
    public Invoice updateInvoice(UpdateInvoiceRequest updateInvoiceRequest) throws Exception {
        Invoice invoice = invoiceDao.findById(updateInvoiceRequest.getId())
                .orElseThrow(Exception::new);
        Company receivedBy = companyMapper.fromDto(updateInvoiceRequest.getReceivedBy());
        List<Product> products = productMapper.fromDto(updateInvoiceRequest.getProducts());

        invoice.setInvoiceNumber(updateInvoiceRequest.getInvoiceNumber());
        invoice.setIssuedIn(updateInvoiceRequest.getIssuedIn());
        invoice.setPaymentMethod(updateInvoiceRequest.getPaymentMethod());
        invoice.setCurrency(updateInvoiceRequest.getCurrency());
        invoice.setIssuedDate(updateInvoiceRequest.getIssuedDate());
        invoice.setDaysTillPayment(updateInvoiceRequest.getDaysTillPayment());
        invoice.setReceivedBy(receivedBy);
        invoice.setProducts(products);
        invoice.setPaidAmount(updateInvoiceRequest.getPaidAmount());
        invoice.setPaid(updateInvoiceRequest.isPaid());

        return invoiceDao.save(invoice);
    }

    @Transactional
    public Invoice getInvoice(Long id) throws Exception {
        return invoiceDao.findById(id)
                .orElseThrow(Exception::new);
    }

    @Transactional
    public List<Invoice> getAllInvoices() {
        return invoiceDao.findAll();
    }

    @Transactional
    public Invoice payForInvoice(Long id) throws Exception {
        Invoice invoice = invoiceDao.findById(id)
                .orElseThrow(Exception::new);

        invoice.setPaid(true);

        return invoiceDao.save(invoice);
    }

    @Transactional
    public void deleteInvoice(Long id) throws Exception {
        Invoice invoice = invoiceDao.findById(id)
                .orElseThrow(Exception::new);

        invoiceDao.delete(invoice);
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