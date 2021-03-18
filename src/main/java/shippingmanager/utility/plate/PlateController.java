package shippingmanager.utility.plate;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/plate")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlateController {

    private final PlateService plateService;


    @DeleteMapping("/deletePlates")
    public void deletePlates(@RequestBody List<PlateDto> platesDto) throws Exception {
        plateService.deletePlates(platesDto);
    }

}