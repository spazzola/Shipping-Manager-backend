package shippingmanager.order;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shippingmanager.company.CompanyController;
import shippingmanager.pdf.PdfOrderService;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayInputStream;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final PdfOrderService pdfOrderService;
    private Logger logger = LogManager.getLogger(OrderController.class);

    public OrderController(OrderMapper orderMapper, OrderService orderService, PdfOrderService pdfOrderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
        this.pdfOrderService = pdfOrderService;
    }

    @PostMapping("/createOrder")
    public OrderDto createOrder(@RequestBody CreateOrderRequest createOrderRequest) throws BadAttributeValueExpException {
        logger.info("Dodawanie zamowienia: " + createOrderRequest);
        Order order = orderService.createOrder(createOrderRequest);

        return orderMapper.toDto(order);
    }

    @GetMapping(value = "/createPdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> create(@RequestParam(value = "id") Long id) throws Exception {
        logger.info("Generowanie PDF dla zamowienia o id: " + id);
        ByteArrayInputStream orderPdfBytes = pdfOrderService.generatePdf(id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(orderPdfBytes));
    }

    @GetMapping("/getOrder")
    public OrderDto getOrder(@RequestParam("id") Long id) throws Exception {
        Order order = orderService.getOrder(id);

        return orderMapper.toDto(order);
    }

    @GetMapping("/getAll")
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        return orderMapper.toDto(orders);
    }

    @PutMapping("/update")
    public OrderDto updateOrder(@RequestBody UpdateOrderRequest updateOrderRequest) throws Exception {
        logger.info("Aktualizowanie zamowienia: " + updateOrderRequest);
        Order order = orderService.updateOrder(updateOrderRequest);

        return orderMapper.toDto(order);
    }

    @DeleteMapping("/delete")
    public void deleteOrder(@RequestParam("id") Long id) throws Exception {
        logger.info("Usuwanie zamowienia o id: " + id);
        orderService.deleteOrder(id);
    }

}