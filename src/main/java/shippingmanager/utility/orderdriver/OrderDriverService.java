package shippingmanager.utility.orderdriver;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.order.Order;
import shippingmanager.utility.driver.Driver;

@AllArgsConstructor
@Service
public class OrderDriverService {

    private final OrderDriverDao orderDriverDao;


    public List<OrderDriver> createOrderDrivers(List<Driver> drivers, Order order) {
        List<OrderDriver> orderDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            OrderDriver orderDriver = new OrderDriver();
            orderDriver.setDriver(driver);
            orderDriver.setOrder(order);
            orderDrivers.add(orderDriver);
            orderDriverDao.save(orderDriver);

        }
        return orderDrivers;
    }

}
