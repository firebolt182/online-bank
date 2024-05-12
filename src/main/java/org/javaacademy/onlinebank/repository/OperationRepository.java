package org.javaacademy.onlinebank.repository;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.AccountDto;
import org.javaacademy.onlinebank.dto.OperationDto;
import org.javaacademy.onlinebank.entity.Operation;
import org.javaacademy.onlinebank.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationRepository {
    private Map<UUID, Operation> operations = new HashMap<>();
    private final AccountRepository accountRepository;

    public void add(Operation operation) {
        operations.put(operation.getId(), operation);
    }

    public Set<OperationDto> findAllOperationsByAccount(String accountNumber) {
        Set<OperationDto> founded = createTreeSet();
        Set<OperationDto> operationSet = operations.values().stream()
                .map(this::convertToDto)
                .filter(operation -> operation.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toSet());
        founded.addAll(operationSet);
        return founded;
    }

    public Set<OperationDto> findUserOperations(User user) {
        Set<OperationDto> founded = createTreeSet();
        List<AccountDto> userAccounts = accountRepository.findAllUserAccounts(user);
        for (AccountDto acc : userAccounts) {
            operations.values().stream()
                    .map(this::convertToDto)
                    .filter(operation -> operation.getAccountNumber()
                            .equals(acc.getAccountNumber()))
                    .forEach(founded::add);
        }
        return founded;
    }

    private Set<OperationDto> createTreeSet() {
        return new TreeSet<>(((o1, o2) -> o2.getLocalDateTime().compareTo(o1.getLocalDateTime())));
    }

    private OperationDto convertToDto(Operation operation) {
        return new OperationDto(
                operation.getId(),
                operation.getLocalDateTime(),
                operation.getAccountNumber(),
                operation.getType(),
                operation.getAmount(),
                operation.getDescription()
        );
    }
}

