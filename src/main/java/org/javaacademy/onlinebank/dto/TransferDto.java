package org.javaacademy.onlinebank.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;

@Data
public class TransferDto {
    @NonNull
    private String token;
    @NonNull
    private String accountNumber;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String description;

}
