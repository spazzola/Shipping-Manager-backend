package shippingmanager.utility.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    private final ProductService productService;
    private final Logger logger;


    @DeleteMapping("/deleteProducts")
    public void deleteProducts(@RequestBody List<ProductDto> productsDto) throws Exception {
        logger.info("Usuwanie produktow: " + productsDto);
        productService.deleteProducts(productsDto);
    }

}