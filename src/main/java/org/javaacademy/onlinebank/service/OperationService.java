package org.javaacademy.onlinebank.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.OperationDto;
import org.javaacademy.onlinebank.entity.Operation;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.OperationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;

    public void add(Operation operation) {
        operationRepository.add(operation);
    }

    public Set<OperationDto> findAllOperationsByAccount(String accountNumber) {
        return operationRepository.findAllOperationsByAccount(accountNumber);
    }

    public Set<OperationDto> findUserOperations(User user) {
        return operationRepository.findUserOperations(user);
    }
}
