package org.javaacademy.onlinebank.entity;

import java.util.UUID;
import lombok.Data;
import lombok.NonNull;

@Data
public class User {
    @NonNull
    private UUID id;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String name;
}
