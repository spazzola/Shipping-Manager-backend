package shippingmanager.utility.bankaccount;

import org.springframework.stereotype.Component;
import shippingmanager.company.Company;
import shippingmanager.company.CompanyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BankAccountMapper {

    public BankAccountDto toDto(BankAccount bankAccount) {

        return BankAccountDto.builder()
                .id(bankAccount.getId())
                .accountName(bankAccount.getAccountName())
                .accountNumber(bankAccount.getAccountNumber())
                .type(bankAccount.getType())
                .build();
    }

    public List<BankAccountDto> toDto(List<BankAccount> bankAccounts) {
        return bankAccounts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<BankAccount> fromDto(CompanyDto companyDto) {
        final List<BankAccountDto> bankAccountsDto = companyDto.getBankAccounts();
        final List<BankAccount> bankAccounts = new ArrayList<>();

        for (BankAccountDto bankAccountDto : bankAccountsDto) {
            bankAccounts.add(BankAccount.builder()
                    .id(bankAccountDto.getId())
                    .accountName(bankAccountDto.getAccountName())
                    .accountNumber(bankAccountDto.getAccountNumber())
                    .type(bankAccountDto.getType())
                    .build());
        }
        return bankAccounts;
    }

    public List<BankAccount> fromDto(CompanyDto companyDto, Company company) {
        final List<BankAccountDto> bankAccountsDto = companyDto.getBankAccounts();
        final List<BankAccount> bankAccounts = new ArrayList<>();

        for (BankAccountDto bankAccountDto : bankAccountsDto) {
            bankAccounts.add(BankAccount.builder()
                    .id(bankAccountDto.getId())
                    .accountName(bankAccountDto.getAccountName())
                    .accountNumber(bankAccountDto.getAccountNumber())
                    .type(bankAccountDto.getType())
                    .company(company)
                    .build());
        }
        return bankAccounts;
    }

}
