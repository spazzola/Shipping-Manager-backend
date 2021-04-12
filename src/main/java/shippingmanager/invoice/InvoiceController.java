package shippingmanager.invoice;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shippingmanager.pdf.PdfInvoiceService;

import java.io.ByteArrayInputStream;

@AllArgsConstructor
@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfInvoiceService pdfInvoiceService;
    private final InvoiceMapper invoiceMapper;


    @PostMapping("/createInvoiceToOrder")
    public Invoice createInvoiceToOrder(@RequestBody CreateInvoiceToOrderRequest createInvoiceToOrderRequest) {

        return invoiceService.createInvoice(createInvoiceToOrderRequest);
    }

    @PostMapping("createInvoice")
    public Invoice createInvoice(@RequestBody CreateInvoiceRequest createInvoiceRequest) {
        return invoiceService.createInvoice(createInvoiceRequest);
    }


    @GetMapping(value = "/createPdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> generatePdf(@RequestParam("id") Long id) throws Exception {
        ByteArrayInputStream invoicePdfBytes = pdfInvoiceService.generatePdf(id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(invoicePdfBytes));
    }

    @GetMapping("/getAll")
    public List<InvoiceDto> getAll() {
        List<Invoice> invoices = invoiceService.getAllInvoices();

        return invoiceMapper.toDto(invoices);
    }

    @PutMapping("/payForInvoice")
    public InvoiceDto payForInvoice(@RequestParam("id") Long id) throws Exception {
        Invoice invoice = invoiceService.payForInvoice(id);

        return invoiceMapper.toDto(invoice);
    }

    @DeleteMapping("/delete")
    public void deleteInvoice(@RequestParam("id") Long id) throws Exception {
        invoiceService.deleteInvoice(id);
    }

}