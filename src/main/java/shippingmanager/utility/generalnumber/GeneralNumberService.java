package shippingmanager.utility.generalnumber;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class GeneralNumberService {

    public String generateNumber(LocalDateTime localDateTime, int number) {
        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();

        return number + "/" + month + "/" + year;
    }

}