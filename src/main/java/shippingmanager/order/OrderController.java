package shippingmanager.order;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shippingmanager.pdf.PdfOrderService;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayInputStream;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final PdfOrderService pdfOrderService;

    @PostMapping("/createOrder")
    public OrderDto createOrder(@RequestBody CreateOrderRequest createOrderRequest) throws BadAttributeValueExpException {
        Order order = orderService.createOrder(createOrderRequest);

        return orderMapper.toDto(order);
    }

    @GetMapping(value = "/createPdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> create(@RequestParam(value = "id") Long id) throws Exception {
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
        Order order = orderService.updateOrder(updateOrderRequest);

        return orderMapper.toDto(order);
    }

    @DeleteMapping("/delete")
    public void deleteOrder(@RequestParam("id") Long id) throws Exception {
        orderService.deleteOrder(id);
    }

}