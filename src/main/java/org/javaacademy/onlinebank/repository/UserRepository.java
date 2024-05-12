package org.javaacademy.onlinebank.repository;

import java.util.*;
import org.javaacademy.onlinebank.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    public User createUser(String phoneNumber, String name) {
        UUID id = UUID.randomUUID();
        User user = new User(id, phoneNumber, name);
        users.put(id, user);
        return user;
    }

    public Optional<User> findByKey(UUID key) {
        return Optional.ofNullable(users.get(key));
    }

    public Optional<User> findByPhone(String phoneNumber) {
        return users.values().stream()
                .filter(user -> user.getPhoneNumber().equals(phoneNumber))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
