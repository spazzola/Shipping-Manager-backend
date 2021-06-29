package shippingmanager.utility.plate;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/plate")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlateController {

    private final PlateService plateService;
    private Logger logger = LogManager.getLogger(PlateController.class);

    public PlateController(PlateService plateService) {
        this.plateService = plateService;
    }

    @DeleteMapping("/deletePlates")
    public void deletePlates(@RequestBody List<PlateDto> platesDto) throws Exception {
        logger.info("Usuwanie tablic rejestracyjnych: " + platesDto);
        plateService.deletePlates(platesDto);
    }

}