package shippingmanager.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT * FROM invoices i " +
            "WHERE MONTH(i.issued_Date) = ?1 AND YEAR(i.issued_Date) = ?2 " +
            "ORDER BY invoice_id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Invoice> findByMonthAndYear(int month, int year);

}