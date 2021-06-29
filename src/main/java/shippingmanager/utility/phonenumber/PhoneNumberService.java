package shippingmanager.utility.phonenumber;

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

    public boolean validatePhoneNumbers(List<PhoneNumberDto> phoneNumbers) {
        for (PhoneNumberDto phoneNumberDto : phoneNumbers) {
            if (phoneNumberDto.getNumber() == null || phoneNumberDto.getNumber().equals("")) {
                return false;
            }
            if (phoneNumberDto.getType() == null || phoneNumberDto.getType().equals("")) {
                return false;
            }
        }
        return true;
    }

}