package org.javaacademy.onlinebank.service;

import java.util.Random;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import static org.javaacademy.onlinebank.service.UserService.TOKEN_POSTFIX;
import static org.javaacademy.onlinebank.service.UserService.TOKEN_PREFIX;

@Service
public class UserServiceHelper {

    public String generatePinCode() {
        StringBuilder pin = new StringBuilder();
        Stream.generate(() -> new Random()
                        .nextInt(10))
                .limit(4).forEach(pin::append);
        return pin.toString();
    }

    public String cutIdFromToken(String token) {
        return token.replace(TOKEN_PREFIX, "").replace(TOKEN_POSTFIX, "");
    }
}
