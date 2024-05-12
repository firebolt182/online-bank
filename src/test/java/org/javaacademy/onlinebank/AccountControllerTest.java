package org.javaacademy.onlinebank;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.javaacademy.onlinebank.config.BankProperties;
import org.javaacademy.onlinebank.repository.AccountRepository;
import org.javaacademy.onlinebank.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor
public class AccountControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankProperties properties;
    private static final String BASE_URL = "http://localhost:";
    private static final String BASE_URL_POSTFIX = "/account";

    @Test
    @DisplayName("Создание счета")
    public void createAccountSuccess() {
        createAccount();
        Assertions.assertEquals(1, accountRepository.size());

    }

    @Test
    @DisplayName("Проверка баланса")
    public void getBalanceSuccess() {
        createAccount();
        Assertions.assertEquals(BigDecimal.ZERO,
                accountRepository.findAccountByNumber("000001")
                        .orElseThrow()
                        .getBalance());
    }

    @Test
    @DisplayName("Вывод счетов пользователя")
    public void findUserAccountsSuccess() {
        String token = createAccount();
        RestAssured
                .given()
                .queryParam("token", token)
                .log().all()
                .get(baseUrl())
                .then()
                .log().all()
                .statusCode(302)
                .body("[0].accountNumber", Matchers.equalTo("000001"))
                .body("user[0].id", Matchers.equalTo(token
                        .replace("token", "")
                        .replace("online", "")))
                .body("user[0].phoneNumber", Matchers.equalTo("+71112223344"))
                .body("user[0].name", Matchers.equalTo("Test"))
                .body("[0].balance", Matchers.equalTo(0));

        RestAssured
                .given()
                .queryParam("token", token)
                .get(baseUrl())
                .then()
                .time(Matchers.lessThan(1L), TimeUnit.SECONDS);

    }

    @SneakyThrows
    private String createAccount() {
        String pin = userService.registration("+71112223344", "Test");
        String token = userService.authentication("+71112223344", pin);

        RestAssured
                .given()
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .log().all()
                .post(baseUrl())
                .then()
                .statusCode(201);
        return token;
    }

    private String baseUrl() {
        return BASE_URL + properties.getPort() + BASE_URL_POSTFIX;
    }
}