package shippingmanager.utility.orderdriver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDriverDao extends JpaRepository<OrderDriver, Long> {

    OrderDriver findByOrderId(Long id);

}