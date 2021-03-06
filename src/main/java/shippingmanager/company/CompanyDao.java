package shippingmanager.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDao extends JpaRepository<Company, Long> {

    Company findByIsMainCompanyTrue();
    Company findByCompanyName(String companyName);

}
