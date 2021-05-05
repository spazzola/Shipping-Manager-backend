package shippingmanager.utility.product;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    private final ProductService productService;


    @DeleteMapping("/deleteProducts")
    public void deleteProducts(@RequestBody List<ProductDto> productsDto) throws Exception {
        productService.deleteProducts(productsDto);
    }

}