package org.javaacademy.onlinebank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.config.BankProperties;
import org.javaacademy.onlinebank.dto.OperationDto;
import org.javaacademy.onlinebank.dto.PaymentDto;
import org.javaacademy.onlinebank.dto.TransferDto;
import org.javaacademy.onlinebank.entity.Operation;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.type.Type;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankService {
    private final UserService userService;
    private final AccountService accountService;
    private final OperationService operationService;
    private final TransferService transferService;
    private final BankProperties properties;

    public void makePayment(PaymentDto dto) {
        dto.setDescription(checkDescription(dto));
        User user = findUserByToken(dto.getToken());
        if (!accountService.checkBelonging(user, dto.getAccountNumber())) {
            throw new RuntimeException("Введенный номер счета не принадлежит пользователю");
        }
        accountService.withDrawMoney(dto.getAccountNumber(), dto.getAmount());
        createOperation(dto.getAccountNumber(),
                        dto.getAmount(),
                        Type.CHARGE_OFF,
                        dto.getDescription());
    }

    public void transferToAnotherBank(String token,
                                      BigDecimal amount,
                                      String accountNumber,
                                      String description) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setToken(token);
        paymentDto.setAmount(amount);
        paymentDto.setAccountNumber(accountNumber);
        paymentDto.setDescription(description);
        makePayment(paymentDto);
        User user = accountService.findUserByAccountNumber(accountNumber);
        transferService.transferToAnotherBank(new TransferDto(
                properties.getName(),
                user.getName(),
                amount,
                description));
    }
    
    public void depositMoney(PaymentDto dto) {
        dto.setDescription(checkDescription(dto));
        accountService.putMoney(dto.getAccountNumber(), dto.getAmount());
        createOperation(dto.getAccountNumber(), dto.getAmount(),
                Type.DEPOSIT, dto.getDescription());
    }

    public Set<OperationDto> showUserHistory(String token) {
        User user = findUserByToken(token);
        return operationService.findUserOperations(user);
    }

    private User findUserByToken(String token) {
        return userService.findByToken(token);
    }

    private void createOperation(String accountNumber,
                                 BigDecimal amount,
                                 Type type,
                                 String description) {
        operationService.add(new Operation(
                UUID.randomUUID(),
                LocalDateTime.now(),
                accountNumber,
                type,
                amount,
                description));
    }

    private String checkDescription(PaymentDto dto) {
        return dto.getDescription() != null ? dto.getDescription() : "";
    }
}
