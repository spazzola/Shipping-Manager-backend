package shippingmanager.pdf;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/pdf")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PdfController {

    private PdfOrderService pdfOrderService;

    @PostMapping("/create")
    public void create(@RequestParam(value = "id") String id) throws Exception {
        pdfOrderService.generatePdf();

    }
}
