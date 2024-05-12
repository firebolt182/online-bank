package org.javaacademy.onlinebank.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.javaacademy.onlinebank.dto.AccountDto;
import org.javaacademy.onlinebank.entity.Account;
import org.javaacademy.onlinebank.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AccountRepository {
    private Map<String, Account> accounts = new HashMap<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Optional<Account> findAccountByNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    public List<AccountDto> findAllUserAccounts(User user) {
        return accounts.values().stream()
                .map(this::convertToDto)
                .filter(account -> account.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public User findUserByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber).getUser();
    }

    public Integer size() {
        return accounts.size();
    }

    private AccountDto convertToDto(Account account) {
        return new AccountDto(account.getAccountNumber(),
                            account.getUser(),
                            account.getBalance());
    }
}
