package shippingmanager.utility.phonenumber;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/phoneNumber")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;


    @DeleteMapping("/deletePhoneNumbers")
    public void deletePhoneNumbers(@RequestBody List<PhoneNumberDto> phoneNumbersDto) throws Exception {
        phoneNumberService.deletePhoneNumbers(phoneNumbersDto);
    }

}