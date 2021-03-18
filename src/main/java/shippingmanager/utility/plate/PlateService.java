package shippingmanager.utility.plate;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PlateService {

    private final PlateDao plateDao;


    @Transactional
    public void deletePlates(List<PlateDto> platesDto) throws Exception {
        for (PlateDto plateDto : platesDto) {
            Plate plate = plateDao.findById(plateDto.getId())
                    .orElseThrow(Exception::new);
            plateDao.delete(plate);
        }
    }
}
