package shippingmanager.utility.orderdriver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDriverDao extends JpaRepository<OrderDriver, Long> {

    List<OrderDriver> findByOrderId(Long id);

}