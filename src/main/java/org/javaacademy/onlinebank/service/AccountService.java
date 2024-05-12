package org.javaacademy.onlinebank.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.AccountDto;
import org.javaacademy.onlinebank.entity.Account;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserService userService;
    private Integer counter = 0;
    private final AccountRepository accountRepository;

    private String generateAccountNumber() {
        counter++;
        return String.format("%06d", counter);
    }

    public Account createAccount(User user) {
        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, user, BigDecimal.ZERO);
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

    public List<AccountDto> findUserAccounts(User user) {
        return accountRepository.findAllUserAccounts(user);
    }

    public BigDecimal showBalance(String accountNumber,
                                  @RequestParam(name = "token") String token) {
        User user = userService.findByToken(token);
        if (!checkBelonging(user, accountNumber)) {
            throw new RuntimeException("Ошибка. Не верно указаны данные");
        }
        return checkAccount(accountNumber).getBalance();
    }

    public boolean checkBelonging(User user, String accountNumber) {
        return findUserAccounts(user).stream()
                .anyMatch(account -> account.getAccountNumber()
                        .equals(accountNumber));
    }

    public User findUserByAccountNumber(String accountNumber) {
        return accountRepository.findUserByAccountNumber(accountNumber);
    }

    private Account checkAccount(String accountNumber) {
        return accountRepository.findAccountByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException(String.format(
                        "Cчет с номером: %s не существует", accountNumber)));
    }
}
