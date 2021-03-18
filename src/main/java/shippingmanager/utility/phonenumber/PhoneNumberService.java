package shippingmanager.utility.phonenumber;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PhoneNumberService {

    private final PhoneNumberDao phoneNumberDao;


    @Transactional
    public void deletePhoneNumbers(List<PhoneNumberDto> phoneNumbersDto) throws Exception {
        for (PhoneNumberDto phoneNumberDto : phoneNumbersDto) {
            PhoneNumber phoneNumber = phoneNumberDao.findById(phoneNumberDto.getId())
                    .orElseThrow(Exception::new);
            phoneNumberDao.delete(phoneNumber);
        }
    }

}