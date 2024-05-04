package org.javaacademy.onlinebank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.javaacademy.onlinebank.type.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Operation {
    @NonNull
    private UUID id;
    @NonNull
    private LocalDateTime localDateTime;
    @NonNull
    private String accountNumber;
    @NonNull
    private Type type;
    @NonNull
    private BigDecimal amount;
    private String description;
}
