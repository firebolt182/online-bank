package org.javaacademy.onlinebank.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationRepository {
    private Map<UUID, String> data = new HashMap<>();

    public void add(UUID id, String pin) {
        data.put(id, pin);
    }

    public boolean authentication(UUID id, String pin) {
        return data.containsKey(id) && data.containsValue(pin);
    }

    public String getPinById(UUID id) {
        return data.get(id);
    }
}