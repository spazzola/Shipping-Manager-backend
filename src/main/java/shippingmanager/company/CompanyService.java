package shippingmanager.company;

import com.sun.tools.javac.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.utility.phonenumber.PhoneNumber;

@AllArgsConstructor
@Service
public class CompanyService {

    private CompanyDao companyDao;
    private CompanyMapper companyMapper;

    @Transactional
    public Company createCompany(CompanyDto companyDto) {
        final Company company = companyMapper.fromDto(companyDto);

        company.getPhoneNumbers().
                forEach(phoneNumber -> phoneNumber.setCompany(company));

        company.getBankAccounts().
                forEach(bankAccount -> bankAccount.setCompany(company));

        return companyDao.save(company);
    }

}
