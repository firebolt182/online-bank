package org.javaacademy.onlinebank;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.javaacademy.onlinebank.config.BankProperties;
import org.javaacademy.onlinebank.dto.AuthDtoRq;
import org.javaacademy.onlinebank.dto.UserDto;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.repository.AuthenticationRepository;
import org.javaacademy.onlinebank.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@AutoConfigureMockMvc
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
    private static final String BASE_URL = "http://localhost:";
    private static final String BASE_URL_POSTFIX = "/user";
    private static final String SIGNUP = "/signup";
    private static final String AUTH = "/auth";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationRepository authenticationRepository;
    @Autowired
    private BankProperties properties;

    @Test
    @DisplayName("Создание пользователя")
    void singUpSuccess() {
        createUser();
        User user = userRepository.findByPhone("+79001234567").orElseThrow();
        Assertions.assertEquals("Test", user.getName());
        Assertions.assertEquals(1, userRepository.findAll().size());

    }

    @Test
    @SneakyThrows
    @DisplayName("Аутентификация")
    void authSuccess() {
        createUser();
        User user = userRepository.findByPhone("+79001234567").orElseThrow();
        String pin = authenticationRepository.getPinById(user.getId());
        String jsonData = objectMapper
                .writeValueAsString(new AuthDtoRq("+79001234567", pin));
        RestAssured
                .given()
                .body(jsonData)
                .contentType(ContentType.JSON)
                .log().all()
                .post(baseUrl() + AUTH)
                .then()
                .log().all()
                .statusCode(202);
    }

    @SneakyThrows
    private void createUser() {
        String jsonData = objectMapper
                .writeValueAsString(new UserDto("+79001234567", "Test"));
        RestAssured
                .given()
                .body(jsonData)
                .contentType(ContentType.JSON)
                .post(baseUrl() + SIGNUP)
                .then()
                .statusCode(201);
    }

    private String baseUrl() {
        return BASE_URL + properties.getPort() + BASE_URL_POSTFIX;
    }
}
