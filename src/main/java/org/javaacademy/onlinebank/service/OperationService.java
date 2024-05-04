package org.javaacademy.onlinebank.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.OperationDto;
import org.javaacademy.onlinebank.entity.Operation;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.OperationRepository;
import org.javaacademy.onlinebank.type.Type;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;

    public void add(Operation operation) {
        operationRepository.add(operation);
    }

    public Set<Operation> findAll() {
        return operationRepository.findAll();
    }

    public Set<Operation> findUserOperations(User user) {
        return operationRepository.findUserOperations(user);
    }
}
