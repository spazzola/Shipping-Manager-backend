package shippingmanager.utility.orderdriver;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.order.Order;
import shippingmanager.utility.driver.Driver;
import shippingmanager.utility.phonenumber.PhoneNumber;
import shippingmanager.utility.plate.Plate;

@AllArgsConstructor
@Service
public class OrderDriverService {

    private final OrderDriverDao orderDriverDao;


    public List<OrderDriver> createAndSaveOrderDrivers(List<Driver> drivers, Order order) {
        List<OrderDriver> orderDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            OrderDriver orderDriver = new OrderDriver();
            orderDriver.setName(driver.getName());
            orderDriver.setSurname(driver.getSurname());
            setPlatesToOrderDriver(orderDriver, driver.getPlates());
            setPhonesToOrderDriver(orderDriver, driver.getPhoneNumbers());
            orderDriver.setOrder(order);
            orderDrivers.add(orderDriver);
        }
//        List<OrderDriver> orderDrivers = new ArrayList<>();
//        for (Driver driver : drivers) {
//            OrderDriver orderDriver = new OrderDriver();
//            orderDriver.setDriver(driver);
//            orderDriver.setOrder(order);
//            orderDrivers.add(orderDriver);
//            orderDriverDao.save(orderDriver);
//        }
        return orderDriverDao.saveAll(orderDrivers);
    }

    private void setPlatesToOrderDriver(OrderDriver orderDriver, List<Plate> plates) {
        if (plates.size() == 1) {
            orderDriver.setFirstPlate(plates.get(0).getPlateNumber());
        }

        if (plates.size() >= 2) {
            orderDriver.setFirstPlate(plates.get(0).getPlateNumber());
            orderDriver.setSecondPlate(plates.get(1).getPlateNumber());
        }
    }

    private void setPhonesToOrderDriver(OrderDriver orderDriver, List<PhoneNumber> phoneNumbers) {
        if (phoneNumbers.size() == 1) {
            orderDriver.setFirstPhoneNumber(phoneNumbers.get(0).getNumber());
        }

        if (phoneNumbers.size() >= 2) {
            orderDriver.setFirstPhoneNumber(phoneNumbers.get(0).getNumber());
            orderDriver.setSecondPhoneNumber(phoneNumbers.get(1).getNumber());
        }
    }
}