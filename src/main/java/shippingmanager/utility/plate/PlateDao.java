package shippingmanager.utility.plate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlateDao extends JpaRepository<Plate, Long> {

}
