package org.javaacademy.onlinebank.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationRepository {
    private Map<UUID, StringBuilder> data = new HashMap<>();

    public void add(UUID id, StringBuilder pin) {
        data.put(id, pin);
    }

    public boolean authentication(UUID id, StringBuilder pin) {
        if (data.containsKey(id) && data.containsValue(pin)) {
            return true;
        }
        return false;
    }
}
