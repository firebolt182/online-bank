package org.javaacademy.onlinebank.repository;

import org.javaacademy.onlinebank.entity.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public void createUser(String phoneNumber, String name) {
        UUID id = UUID.randomUUID();
        User user = new User(id, phoneNumber, name);
        users.put(id.toString(), user);
    }

    public Optional<User> findByKey(String key) {
        return Optional.ofNullable(users.get(key));
    }

    public Optional<User> findByPhone(String phoneNumber) {
        return users.values().stream()
                .filter(user -> user.getPhoneNumber().equals(phoneNumber))
                .findFirst();
    }

}
