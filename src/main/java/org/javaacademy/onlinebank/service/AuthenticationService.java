package org.javaacademy.onlinebank.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.repository.AuthenticationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    AuthenticationRepository authenticationRepository;

    public void write(UUID id, StringBuilder pin) {
        authenticationRepository.add(id, pin);
    }

    public boolean authentication(UUID id, StringBuilder pin) {
        return authenticationRepository.authentication(id, pin);
    }

}
