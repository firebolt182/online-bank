package org.javaacademy.onlinebank.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String ONLINE = "online";
    private static final String TOKEN = "token";

    public StringBuilder registration(String phoneNumber, String name) {
        //Необходимо сделать запись в сервисе аутентификации(п. 6.1)
        //6.1 У сервиса аутентификации есть метод - добавить новую запись.
        //Добавляется пара уникальный индентификатор + пинкод (число из 4 цифр).
        if (userRepository.findByPhone(phoneNumber).isPresent()) {
            throw new RuntimeException("Пользователь с таким телефоном уже существует");
        }
        userRepository.createUser(phoneNumber, name);
        return generatePin();
    }

    public String authentication(String phoneNumber, StringBuilder pinCode) {
        User user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        UUID id = user.getId();
        //здесь должен быть вызов метода из п. 6.2
        //6.2 У сервиса аутентификации есть метод - аутентификация:
        // на вход передается уникальный индентификатор, пинкод.
        // На выход передается результат проверки:
        // есть запись с таким уникальным индентификатором
        // и пинкод по этому идентификатору совпадает с переданным пинкодом.
        //Пример:
        //В сервис добавили запись: 'aaa-aaa-aaa-aaaa': 1234.
        //Вызывают метод аутентификация с параметрами: 'aaa-aaa-aaa-aaaa', 5552.
        // Сервис вернет false, так как по данному уникальному идентификатору пинкод 1234.
        // 1234 не равен 5552.
        //Вызывают метод аутентификация с параметрами: 'aaa-aaa-aaa-aaaa',
        // 1234. Сервис вернет true, так как по данному уникальному идентификатору
        // хранится пинкод 1234.
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
