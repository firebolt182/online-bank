package org.javaacademy.onlinebank.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.javaacademy.onlinebank.entity.User;

@Data
@AllArgsConstructor
public class AccountDto {
    @NonNull
    private String accountNumber;
    @NonNull
    private User user;
    @NonNull
    private BigDecimal balance;
}
