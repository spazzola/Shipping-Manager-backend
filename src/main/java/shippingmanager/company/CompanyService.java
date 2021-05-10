package shippingmanager.company;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.utility.address.Address;
import shippingmanager.utility.address.AddressMapper;
import shippingmanager.utility.bankaccount.BankAccount;
import shippingmanager.utility.bankaccount.BankAccountMapper;
import shippingmanager.utility.phonenumber.PhoneNumber;
import shippingmanager.utility.phonenumber.PhoneNumberMapper;

@AllArgsConstructor
@Service
public class CompanyService {

    private CompanyDao companyDao;
    private CompanyMapper companyMapper;
    private AddressMapper addressMapper;
    private PhoneNumberMapper phoneNumberMapper;
    private BankAccountMapper bankAccountMapper;

    @Transactional
    public Company createCompany(CompanyDto companyDto) {
        if (validateCreateCompanyRequest(companyDto)) {
            final Company company = companyMapper.fromDto(companyDto);
            if (!checkIfCompanyExist(company)) {
                company.setMainCompany(false);

                company.getPhoneNumbers().
                        forEach(phoneNumber -> phoneNumber.setCompany(company));

                company.getBankAccounts().
                        forEach(bankAccount -> bankAccount.setCompany(company));

                return companyDao.save(company);
            } else {
                return companyDao.findByCompanyName(company.getCompanyName());
            }
        }
        //TODO what if it's not validated? what to return?
        return null;
    }

    @Transactional
    public Company updateCompany(CompanyDto companyDto) throws Exception {
        Company company = companyDao.findById(companyDto.getId())
                .orElseThrow(Exception::new);

        Address address = addressMapper.fromDto(companyDto.getAddress());
        List<PhoneNumber> phoneNumbers = phoneNumberMapper.fromDto(companyDto.getPhoneNumbers());
        List<BankAccount> bankAccounts = bankAccountMapper.fromDto(companyDto.getBankAccounts());

        phoneNumbers.
                forEach(phoneNumber -> phoneNumber.setCompany(company));

        bankAccounts.
                forEach(bankAccount -> bankAccount.setCompany(company));

        company.setCompanyName(companyDto.getCompanyName());
        company.setNip(companyDto.getNip());
        company.setRegon(companyDto.getRegon());
        company.setEmail(companyDto.getEmail());
        company.setAddress(address);
        company.setPhoneNumbers(phoneNumbers);
        company.setBankAccounts(bankAccounts);


        return companyDao.save(company);
    }

    @Transactional
    public void deleteCompany(Long id) throws Exception {
        Company company = companyDao.findById(id)
                .orElseThrow(Exception::new);

        companyDao.delete(company);
    }

    @Transactional
    public Company getCompany(Long id) throws Exception {
        return companyDao.findById(id)
                .orElseThrow(Exception::new);
    }

    @Transactional
    public List<Company> getAll() {
        return companyDao.findAll();
    }

    private boolean checkIfCompanyExist(Company company) {
        return companyDao.findByCompanyName(company.getCompanyName()) != null;
    }

    private boolean validateCreateCompanyRequest(CompanyDto companyDto) {
        if (companyDto.getCompanyName() == null || companyDto.getCompanyName().equals("")) {
            return false;
        }
        if (companyDto.getNip() == null || companyDto.getNip().equals("")) {
            return false;
        }
        if (companyDto.getRegon() == null || companyDto.getRegon().equals("")) {
            return false;
        }
        if (companyDto.getEmail() == null || companyDto.getEmail().equals("")) {
            return false;
        }
        if (companyDto.getAddress() == null) {
            return false;
        }
        if (companyDto.getPhoneNumbers() == null) {
            return false;
        }
        if (companyDto.getBankAccounts() == null) {
            return false;
        }

        return true;
    }

}
