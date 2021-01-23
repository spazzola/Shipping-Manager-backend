package shippingmanager.invoice;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shippingmanager.pdf.PdfInvoiceService;

@AllArgsConstructor
@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfInvoiceService pdfInvoiceService;


    @PostMapping("/createInvoiceToOrder")
    public Invoice createInvoiceToOrder(@RequestBody CreateInvoiceToOrderRequest createInvoiceToOrderRequest) {

        return invoiceService.createInvoice(createInvoiceToOrderRequest);
    }

    @PostMapping("createInvoice")
    public Invoice createInvoice(@RequestBody CreateInvoiceRequest createInvoiceRequest) {
        return invoiceService.createInvoice(createInvoiceRequest);
    }


    @PostMapping("/createpdf")
    public void generatePdf() throws Exception {
        pdfInvoiceService.generatePdf();
    }

}