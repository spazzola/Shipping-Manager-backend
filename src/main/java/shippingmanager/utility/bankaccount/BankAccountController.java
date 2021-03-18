package shippingmanager.utility.bankaccount;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/bankAccount")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BankAccountController {

    private final BankAccountService bankAccountService;


    @DeleteMapping("/deleteBankAccounts")
    public void deleteBankAccounts(@RequestBody List<BankAccountDto> bankAccountsDto) throws Exception {
        bankAccountService.deleteBankAccounts(bankAccountsDto);
    }

}