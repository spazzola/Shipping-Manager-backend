package shippingmanager.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {

    Order findByOrderNumber(String orderNumber);
    Optional<Order> findTopByOrderByIdDesc();

}
