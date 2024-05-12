package org.javaacademy.onlinebank.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class AuthDtoRq {
    @NonNull
    private String phoneNumber;
    @NonNull
    private String pinCode;
}
