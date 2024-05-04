package org.javaacademy.onlinebank.repository;

import com.sun.source.tree.Tree;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.entity.Account;
import org.javaacademy.onlinebank.entity.Operation;
import org.javaacademy.onlinebank.entity.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationRepository {
    private Map<UUID, Operation> operations = new HashMap<>();
    private final AccountRepository accountRepository;

    public void add(Operation operation) {
        operations.put(operation.getId(), operation);
    }

    public Set<Operation> findAll() {
        Set<Operation> founded = new TreeSet<>(((o1, o2) -> o2.getLocalDateTime().compareTo(o1.getLocalDateTime())));
        founded.addAll(operations.values());
        return founded;
    }

    public Set<Operation> findUserOperations(User user) {
        Set<Operation> founded = new TreeSet<>(((o1, o2) -> o2.getLocalDateTime().compareTo(o1.getLocalDateTime())));
        List<Account> userAccounts = accountRepository.findAllUserAccounts(user);
        for (Account acc : userAccounts) {
            operations.values().stream()
                    .filter(operation -> operation.getAccountNumber().equals(acc.getAccountNumber()))
                    .forEach(founded::add);
        }
        return founded;
    }
}

