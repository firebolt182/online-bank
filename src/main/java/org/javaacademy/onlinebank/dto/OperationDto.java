package org.javaacademy.onlinebank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.javaacademy.onlinebank.type.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationDto {
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
