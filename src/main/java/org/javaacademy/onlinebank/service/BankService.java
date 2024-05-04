package org.javaacademy.onlinebank.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.entity.Account;
import org.javaacademy.onlinebank.entity.Operation;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.type.Type;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankService {
    private final UserService userService;
    private final AccountService accountService;
    private final OperationService operationService;

    public void makePayment(String accountNumber, BigDecimal amount, String description, String token) {
        User user = findUserByToken(token);
        if (!accountService.checkBelonging(user, accountNumber)) {
            throw new RuntimeException("Введенный номер счета не принадлежит пользователю");
        }
        accountService.withDrawMoney(accountNumber, amount);
        createOperation(accountNumber, amount, description);
    }
    
    public void depositMoney(String accountNumber, BigDecimal amount, String description) {
        accountService.putMoney(accountNumber,amount);
        createOperation(accountNumber, amount, description);
    }

    public Set<Operation> showUserHistory(String token) {
        User user = findUserByToken(token);
        return operationService.findUserOperations(user);
    }

    private User findUserByToken(String token) {
        return userService.findByToken(token);
    }

    private void createOperation(String accountNumber, BigDecimal amount, String description) {
        operationService.add(new Operation(
                UUID.randomUUID(),
                LocalDateTime.now(),
                accountNumber,
                Type.CHARGE_OFF,
                amount,
                description));
    }
}
