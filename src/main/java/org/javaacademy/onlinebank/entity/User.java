package org.javaacademy.onlinebank.entity;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class User {
    @NonNull
    private UUID id;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String name;
}
