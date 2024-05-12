package org.javaacademy.onlinebank.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PaymentDto {
    private String bankName;
    private String token;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String accountNumber;
    private String description;
}
