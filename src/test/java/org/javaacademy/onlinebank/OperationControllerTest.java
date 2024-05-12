package org.javaacademy.onlinebank;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.javaacademy.onlinebank.config.BankProperties;
import org.javaacademy.onlinebank.dto.PaymentDto;
import org.javaacademy.onlinebank.repository.AccountRepository;
import org.javaacademy.onlinebank.repository.OperationRepository;
import org.javaacademy.onlinebank.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OperationControllerTest {
    @Autowired
    private BankProperties properties;
    private static final String BASE_URL = "http://localhost:";
    private static final String BASE_URL_POSTFIX = "/operations";
    private static final String PAY = "/pay";
    private static final String RECEIVE = "/receive";
    static final String ACCOUNT = "/account";
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    UserService userService;
    @Autowired
    AccountRepository accountService;
    @Autowired
    OperationRepository operationRepository;

    @Test
    @DisplayName("Внесение денег")
    public void depositSuccess() {
        PaymentDto paymentDto = deposit();
        Assertions.assertEquals(accountService.findAccountByNumber(paymentDto.getAccountNumber())
                .orElseThrow().getBalance(), new BigDecimal(500));
    }

    @Test
    @SneakyThrows
    @DisplayName("Платеж")
    public void makePaymentSuccess() {
        PaymentDto paymentDto = deposit();
        String jsonData = objectMapper
                .writeValueAsString(paymentDto);
        RestAssured
                .given()
                .body(jsonData)
                .contentType(ContentType.JSON)
                .post(getBaseUrl() + PAY)
                .then()
                .statusCode(200);

    }

    @Test
    @SneakyThrows
    @DisplayName("Вывод всех операций пользователя")
    public void findUserOperationsSuccess() {
        PaymentDto paymentDto = deposit();
        RestAssured
                .given()
                .queryParam("token", paymentDto.getToken())
                .log().all()
                .get(getBaseUrl())
                .then()
                .log().all()
                .statusCode(302);
        Assertions.assertEquals(operationRepository
                .findUserOperations(userService.findByToken(paymentDto.getToken())).size(), 1);

        RestAssured
                .given()
                .queryParam("token", paymentDto.getToken())
                .get(getBaseUrl())
                .then()
                .time(Matchers.lessThan(1L), TimeUnit.SECONDS);
    }

    @Test
    @SneakyThrows
    @DisplayName("Вывод операций пользователя по счету")
    public void findOperationsByAccountNumberSuccess() {
        PaymentDto paymentDto = deposit();
        RestAssured
                .given()
                .queryParam("accountNumber", paymentDto.getAccountNumber())
                .log().all()
                .get(getBaseUrl() + ACCOUNT)
                .then()
                .log().all()
                .statusCode(302);

        Assertions.assertEquals(operationRepository
                .findAllOperationsByAccount(paymentDto.getAccountNumber()).size(), 1);
    }

    private String createAccount() {
        String pin = userService.registration("+71112223344", "Test");
        String token = userService.authentication("+71112223344", pin);
        RestAssured
                .given()
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .log().all()
                .post(BASE_URL + properties.getPort() + ACCOUNT)
                .then()
                .statusCode(201);
        return token;
    }

    @SneakyThrows
    private PaymentDto deposit() {
        String token = createAccount();
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAccountNumber("000001");
        paymentDto.setAmount(new BigDecimal("500"));
        paymentDto.setToken(token);
        paymentDto.setDescription("test description");
        String jsonData = objectMapper
                .writeValueAsString(paymentDto);
        RestAssured
                .given()
                .body(jsonData)
                .contentType(ContentType.JSON)
                .post(getBaseUrl() + RECEIVE)
                .then()
                .statusCode(200);
        return paymentDto;
    }

    private String getBaseUrl() {
        return BASE_URL + properties.getPort() + BASE_URL_POSTFIX;
        }
}
