package shippingmanager.utility.generalnumber;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneralNumberDao extends JpaRepository<GeneralNumber, Long> {

    Optional<GeneralNumber> findTopByOrderByIdDesc();

}