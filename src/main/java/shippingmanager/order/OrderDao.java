package shippingmanager.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {

    Order findByOrderNumber(String orderNumber);
    Optional<Order> findTopByOrderByIdDesc();

    @Query(value = "SELECT * FROM orders o " +
            "WHERE MONTH(o.created_Date) = ?1 AND YEAR(o.created_Date) = ?2 " +
            "ORDER BY order_id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Order> findByMonthAndYear(int month, int year);

}