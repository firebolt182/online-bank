package org.javaacademy.onlinebank.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private static final String ONLINE = "online";
    private static final String TOKEN = "token";

    public StringBuilder registration(String phoneNumber, String name) {
        if (userRepository.findByPhone(phoneNumber).isPresent()) {
            throw new RuntimeException("Пользователь с таким телефоном уже существует");
        }
        User user = userRepository.createUser(phoneNumber, name);
        StringBuilder pin = generatePin();
        authenticationService.write(user.getId(), pin);
        return pin;
    }

    public String authentication(String phoneNumber, StringBuilder pinCode) {
        User user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        UUID id = user.getId();
        if (!authenticationService.authentication(id, pinCode)) {
            throw new RuntimeException("Некорректный пин-код");
        }
        return ONLINE + user.getId() + TOKEN;
    }

    public User findByToken(String token) {
        String id = cutIdFromToken(token);
        return userRepository.findByKey(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    private StringBuilder generatePin() {
        StringBuilder pin = new StringBuilder();
        Stream.generate(() -> new Random()
                        .nextInt(10))
                        .limit(4).forEach(pin::append);
        return pin;
    }

    private String cutIdFromToken(String token) {
        return token.replace(ONLINE, "").replace(TOKEN, "");
    }
}
