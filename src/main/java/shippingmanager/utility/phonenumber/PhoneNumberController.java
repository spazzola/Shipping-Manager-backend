package shippingmanager.utility.phonenumber;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/phoneNumber")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;
    private Logger logger = LogManager.getLogger(PhoneNumberController.class);

    public PhoneNumberController(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }

    @DeleteMapping("/deletePhoneNumbers")
    public void deletePhoneNumbers(@RequestBody List<PhoneNumberDto> phoneNumbersDto) throws Exception {
        logger.info("Usuwanie numerow telefonow: " + phoneNumbersDto);
        phoneNumberService.deletePhoneNumbers(phoneNumbersDto);
    }

}