package org.javaacademy.onlinebank.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.repository.AuthenticationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationRepository authenticationRepository;

    public void write(UUID id, String pin) {
        authenticationRepository.add(id, pin);
    }

    public boolean authentication(UUID id, String pin) {
        return authenticationRepository.authentication(id, pin);
    }

}
