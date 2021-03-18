package shippingmanager.order;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shippingmanager.pdf.PdfOrderService;

import javax.management.BadAttributeValueExpException;
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

    @PostMapping("/createPdf")
    public void create(@RequestParam(value = "id") Long id) throws Exception {
        pdfOrderService.generatePdf(id);

    }

    @GetMapping("/getAll")
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        return orderMapper.toDto(orders);
    }

    @DeleteMapping("/delete")
    public void deleteOrder(@RequestParam("id") Long id) throws Exception {
        orderService.deleteOrder(id);
    }

}