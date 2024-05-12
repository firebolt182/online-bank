package org.javaacademy.onlinebank.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final UserServiceHelper userServiceHelper;
    static final String TOKEN_PREFIX = "online";
    static final String TOKEN_POSTFIX = "token";

    // TODO: 06.05.2024 ПОМЕНЯЙ ОБРАТНО ПИН 
    public String registration(String phoneNumber, String name) {
        if (userRepository.findByPhone(phoneNumber).isPresent()) {
            throw new RuntimeException("Пользователь с таким телефоном уже существует");
        }
        User user = userRepository.createUser(phoneNumber, name);
        String pin = generatePin();
        //String pin = "1111";
        authenticationService.write(user.getId(), pin);
        return pin;
    }

    public String authentication(String phoneNumber, String pinCode) {
        User user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        UUID id = user.getId();
        if (!authenticationService.authentication(id, pinCode)) {
            throw new RuntimeException("Некорректный пин-код");
        }
        return TOKEN_PREFIX + user.getId() + TOKEN_POSTFIX;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByToken(String token) {
        UUID id = UUID.fromString(cutIdFromToken(token));
        return userRepository.findByKey(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    private String generatePin() {
        return userServiceHelper.generatePinCode();
    }

    private String cutIdFromToken(String token) {
        return userServiceHelper.cutIdFromToken(token);
    }
}
