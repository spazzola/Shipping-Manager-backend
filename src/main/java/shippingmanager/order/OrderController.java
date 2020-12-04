package shippingmanager.order;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;

@AllArgsConstructor
@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody CreateOrderRequest createOrderRequest) throws BadAttributeValueExpException {

        Order order = orderService.createOrder(createOrderRequest);

        return orderMapper.toDto(order);
    }

}
