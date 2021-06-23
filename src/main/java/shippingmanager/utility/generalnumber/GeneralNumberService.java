package shippingmanager.utility.generalnumber;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GeneralNumberService {

    public String generateNumber(LocalDateTime localDateTime, int number) {
        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();

        return number + "/" + month + "/" + year;
    }

}