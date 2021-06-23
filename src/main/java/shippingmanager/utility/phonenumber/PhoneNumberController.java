package shippingmanager.utility.phonenumber;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/phoneNumber")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;
    private final Logger logger;

    @DeleteMapping("/deletePhoneNumbers")
    public void deletePhoneNumbers(@RequestBody List<PhoneNumberDto> phoneNumbersDto) throws Exception {
        logger.info("Usuwanie numerow telefonow: " + phoneNumbersDto);
        phoneNumberService.deletePhoneNumbers(phoneNumbersDto);
    }

}