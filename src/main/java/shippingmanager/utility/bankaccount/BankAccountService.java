package shippingmanager.utility.bankaccount;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class BankAccountService {

    private final BankAccountDao bankAccountDao;


    @Transactional
    public void deleteBankAccounts(List<BankAccountDto> bankAccountsDto) throws Exception {
        for (BankAccountDto bankAccountDto : bankAccountsDto) {
            BankAccount bankAccount = bankAccountDao.findById(bankAccountDto.getId())
                    .orElseThrow(Exception::new);
            bankAccountDao.delete(bankAccount);
        }
    }
}