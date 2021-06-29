package shippingmanager.utility.driver;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public List<Driver> createAndSaveDriversIfNotExists(CreateOrderRequest createOrderRequest) {
        List<DriverDto> driversDtoFromRequest = createOrderRequest.getDrivers();

        for (DriverDto driverFromRequest : driversDtoFromRequest) {
            Driver driverFromDb = driverDao.findByNameAndSurname(driverFromRequest.getName(), driverFromRequest.getSurname());

            if (driverFromDb != null) {
                List<Plate> differentPlates = extractDifferentPlates(driverFromDb, driverFromRequest);
                differentPlates.forEach(plate -> plate.setDriver(driverFromDb));
                driverFromDb.getPlates().addAll(differentPlates);
                driverDao.save(driverFromDb);
            } else {
                createDriver(driverFromRequest);
            }
        }



//        for (Driver driver : drivers) {
//            driver.getPlates().forEach(plate -> plate.setDriver(driver));
//            driver.getPhoneNumbers().forEach(phoneNumber -> phoneNumber.setDriver(driver));
//        }

        List<Driver> drivers = driverMapper.fromDto(driversDtoFromRequest);

        return drivers;
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

    private List<Plate> extractDifferentPlates(Driver driverFromDb, DriverDto driverFromRequest) {
        List<Plate> dbDriverPlates = driverFromDb.getPlates();
        List<Plate> requestDriverPlates = plateMapper.fromDto(driverFromRequest.getPlates());

        List<Plate> allPlates = new ArrayList<>();
        allPlates.addAll(dbDriverPlates);
        allPlates.addAll(requestDriverPlates);

        //List<Plate> result  = allPlates.stream().filter(distinctByKey(Plate::getPlateNumber)).collect(Collectors.toList());

        return allPlates.stream().filter(distinctByPlateNumber(Plate::getPlateNumber)).collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByPlateNumber(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}