package shippingmanager.utility.driver;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/driver")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DriverController {

    private DriverService driverService;
    private DriverMapper driverMapper;


    @PostMapping("/create")
    public DriverDto createDriver(@RequestBody DriverDto driverDto) {
        Driver driver = driverService.createDriver(driverDto);

        return driverMapper.toDto(driver);
    }

    @PutMapping("/update")
    public DriverDto updateDriver(@RequestBody DriverDto driverDto) throws Exception {
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
        driverService.deleteDriver(id);
    }

}