package shippingmanager.utility.driver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverDao extends JpaRepository<Driver, Long> {

    Driver findByNameAndSurname(String name, String surname);

}