package shippingmanager.utility.driver;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/driver")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DriverController {

    private DriverService driverService;
    private DriverMapper driverMapper;
    private Logger logger = LogManager.getLogger(DriverController.class);

    public DriverController(DriverService driverService, DriverMapper driverMapper) {
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @PostMapping("/create")
    public DriverDto createDriver(@RequestBody DriverDto driverDto) {
        logger.info("Dodawanie kierowcy: " + driverDto);
        Driver driver = driverService.createDriver(driverDto);

        return driverMapper.toDto(driver);
    }

    @PutMapping("/update")
    public DriverDto updateDriver(@RequestBody DriverDto driverDto) throws Exception {
        logger.info("Aktualizowanie kierowcy: " + driverDto);
        Driver driver = driverService.updateDriver(driverDto);

        return driverMapper.toDto(driver);
    }

    @GetMapping("/getDriver")
    public DriverDto getDriver(@RequestParam("id") Long id) throws Exception {
        Driver driver = driverService.getDriver(id);

        return driverMapper.toDto(driver);
    }

    @GetMapping("/getAll")
    public List<DriverDto> getAllDrivers() {
        List<Driver> drivers = driverService.getAll();

        return driverMapper.toDto(drivers);
    }

    @DeleteMapping("/deleteDriver")
    public void deleteDriver(@RequestParam("id") Long id) throws Exception {
        logger.info("Usuwanie kierowcy o id: " + id);
        driverService.deleteDriver(id);
    }

}