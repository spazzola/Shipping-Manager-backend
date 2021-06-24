package shippingmanager.utility.bankaccount;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/bankAccount")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private Logger logger = LogManager.getLogger(BankAccountController.class);

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @DeleteMapping("/deleteBankAccounts")
    public void deleteBankAccounts(@RequestBody List<BankAccountDto> bankAccountsDto) throws Exception {
        logger.info("Usuwanie kont bankowych: " + bankAccountsDto);
        bankAccountService.deleteBankAccounts(bankAccountsDto);
    }

}