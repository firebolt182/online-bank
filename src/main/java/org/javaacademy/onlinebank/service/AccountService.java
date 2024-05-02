package org.javaacademy.onlinebank.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.entity.Account;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static Integer accNum = 0;
    private AccountRepository accountRepository;

    private String generateAccountNumber() {
        accNum++;
        return String.format("%06d", accNum);
    }

    public Account createAccount(User user) {
        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, user, new BigDecimal("0.00"));
        accountRepository.add(account);
        return account;
    }

    public void putMoney(String accountNumber, BigDecimal amount) {
        Account account = checkAccount(accountNumber);
        account.setBalance(account.getBalance().add(amount));
    }

    public void withDrawMoney(String accountNumber, BigDecimal amount) {
        Account account = checkAccount(accountNumber);
        if (amount.compareTo(account.getBalance()) > 0) {
            throw new RuntimeException(String.format(
                    "Отказано. Вы хотите снять: %s. Ваш баланс: %s",
                    amount.toPlainString(),
                    account.getBalance().toPlainString()));
        }
        account.setBalance(account.getBalance().subtract(amount));
    }

    public List<Account> findUserAccounts(User user) {
        return accountRepository.findAllUserAccounts(user);
    }

    public BigDecimal showBalance(String accountNumber) {
        Account account = checkAccount(accountNumber);
        return account.getBalance();
    }

    public boolean checkBelonging(User user, String accountNumber) {
        Optional<Account> acc = findUserAccounts(user).stream()
                .filter(account -> account.getAccountNumber()
                        .equals(accountNumber)).findFirst();
        return acc.isPresent();
    }

    private Account checkAccount(String accountNumber) {
        return accountRepository.findAccountByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException(String.format(
                        "Cчет с номером: %s не существует", accountNumber)));
    }
}
