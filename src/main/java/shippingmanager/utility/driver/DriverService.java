package shippingmanager.utility.driver;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.order.CreateOrderRequest;

@AllArgsConstructor
@Service
public class DriverService {

    private final DriverMapper driverMapper;
    private final DriverDao driverDao;

    @Transactional
    public List<Driver> createDrivers(CreateOrderRequest createOrderRequest) {
        List<Driver> drivers = driverMapper.fromDto(createOrderRequest.getDrivers());

        for (Driver driver : drivers) {
            driver.getPlates().forEach(plate -> plate.setDriver(driver));
            driver.getPhoneNumbers().forEach(phoneNumber -> phoneNumber.setDriver(driver));
        }

        return driverDao.saveAll(drivers);
    }


}
