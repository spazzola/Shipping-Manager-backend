package shippingmanager.invoice;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shippingmanager.pdf.PdfInvoiceService;

import java.io.ByteArrayInputStream;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfInvoiceService pdfInvoiceService;
    private final InvoiceMapper invoiceMapper;
    private final Logger logger;


    @PostMapping("/createInvoiceToOrder")
    public InvoiceDto createInvoiceToOrder(@RequestBody CreateInvoiceToOrderRequest createInvoiceToOrderRequest) {
        logger.info("Dodawanie faktury do zamówienia: " + createInvoiceToOrderRequest);
        Invoice invoice = invoiceService.createInvoice(createInvoiceToOrderRequest);

        return invoiceMapper.toDto(invoice);
    }

    @PostMapping("createInvoice")
    public InvoiceDto createInvoice(@RequestBody CreateInvoiceRequest createInvoiceRequest) {
        logger.info("Dodawanie faktury: " + createInvoiceRequest);
        Invoice invoice = invoiceService.createInvoice(createInvoiceRequest);

        return invoiceMapper.toDto(invoice);
    }

    @PutMapping("/update")
    public InvoiceDto updateInvoice(@RequestBody UpdateInvoiceRequest updateInvoiceRequest) throws Exception {
        Invoice invoice = invoiceService.updateInvoice(updateInvoiceRequest);

        return invoiceMapper.toDto(invoice);
    }

    @GetMapping(value = "/createPdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> generatePdf(@RequestParam("id") Long id) throws Exception {
        logger.info("Generowanie PDF dla faktury o id: " + id);
        ByteArrayInputStream invoicePdfBytes = pdfInvoiceService.generatePdf(id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(invoicePdfBytes));
    }

    @GetMapping("/getInvoice")
    public InvoiceDto getInvoice(@RequestParam("id") Long id) throws Exception {
        Invoice invoice = invoiceService.getInvoice(id);

        return invoiceMapper.toDto(invoice);
    }

    @GetMapping("/getAll")
    public List<InvoiceDto> getAll() {
        List<Invoice> invoices = invoiceService.getAllInvoices();

        return invoiceMapper.toDto(invoices);
    }

    @PutMapping("/payForInvoice")
    public InvoiceDto payForInvoice(@RequestParam("id") Long id) throws Exception {
        logger.info("Płacenie za fakture o id: " + id);
        Invoice invoice = invoiceService.payForInvoice(id);

        return invoiceMapper.toDto(invoice);
    }

    @DeleteMapping("/delete")
    public void deleteInvoice(@RequestParam("id") Long id) throws Exception {
        logger.info("Usuwanie faktury o id: " + id);
        invoiceService.deleteInvoice(id);
    }

}