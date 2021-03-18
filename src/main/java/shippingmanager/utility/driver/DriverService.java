package shippingmanager.utility.driver;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.order.CreateOrderRequest;
import shippingmanager.utility.phonenumber.PhoneNumber;
import shippingmanager.utility.phonenumber.PhoneNumberMapper;
import shippingmanager.utility.plate.Plate;
import shippingmanager.utility.plate.PlateMapper;

@AllArgsConstructor
@Service
public class DriverService {

    private final DriverMapper driverMapper;
    private final DriverDao driverDao;
    private final PhoneNumberMapper phoneNumberMapper;
    private final PlateMapper plateMapper;

    @Transactional
    public List<Driver> createDrivers(CreateOrderRequest createOrderRequest) {
        List<Driver> drivers = driverMapper.fromDto(createOrderRequest.getDrivers());

        for (Driver driver : drivers) {
            driver.getPlates().forEach(plate -> plate.setDriver(driver));
            driver.getPhoneNumbers().forEach(phoneNumber -> phoneNumber.setDriver(driver));
        }

        return driverDao.saveAll(drivers);
    }

    @Transactional
    public Driver createDriver(DriverDto driverDto) {
        Driver driver = driverMapper.fromDto(driverDto);

        driver.getPlates().forEach(plate -> plate.setDriver(driver));
        driver.getPhoneNumbers().forEach(phoneNumber -> phoneNumber.setDriver(driver));

        return driverDao.save(driver);
    }

    @Transactional
    public Driver updateDriver(DriverDto driverDto) throws Exception {
        Driver driver = driverDao.findById(driverDto.getId())
                .orElseThrow(Exception::new);
        List<Plate> plates = plateMapper.fromDto(driverDto.getPlates());
        List<PhoneNumber> phoneNumbers = phoneNumberMapper.fromDto(driverDto.getPhoneNumbers());

        plates.
                forEach(plate -> plate.setDriver(driver));

        phoneNumbers.
                forEach(phoneNumber -> phoneNumber.setDriver(driver));

        driver.setName(driverDto.getName());
        driver.setSurname(driverDto.getSurname());
        driver.setPlates(plates);
        driver.setPhoneNumbers(phoneNumbers);

        return driverDao.save(driver);
    }

    @Transactional
    public Driver getDriver(Long id) throws Exception {
        return driverDao.findById(id)
                .orElseThrow(Exception::new);
    }

    @Transactional
    public List<Driver> getAll() {
        return driverDao.findAll();
    }

    @Transactional
    public void deleteDriver(Long id) throws Exception {
        Driver driver = driverDao.findById(id)
                .orElseThrow(Exception::new);

        driverDao.delete(driver);
    }

}