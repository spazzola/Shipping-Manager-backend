package shippingmanager.utility.generalnumber;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GeneralNumberService {

    private GeneralNumberDao generalNumberDao;

    public String generateNumber(LocalDateTime localDateTime) {
        //TODO look for previous number in particular month (current)
        Optional<GeneralNumber> previousNumber = generalNumberDao.findTopByOrderByIdDesc();
        BigDecimal newNumber;

        if (previousNumber.isPresent()) {
            newNumber = previousNumber.get().getNumber().add(BigDecimal.valueOf(1));
        } else {
            newNumber = BigDecimal.valueOf(1);
        }

        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();

        return newNumber + "/" + month + "/" + year;
    }


}
