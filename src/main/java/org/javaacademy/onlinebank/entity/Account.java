package org.javaacademy.onlinebank.entity;

import java.math.BigDecimal;
import lombok.*;

@RequiredArgsConstructor
@Getter
public class Account {
    @NonNull
    private String accountNumber;
    @NonNull
    private User user;
    @NonNull
    @Setter
    private BigDecimal balance;

}
