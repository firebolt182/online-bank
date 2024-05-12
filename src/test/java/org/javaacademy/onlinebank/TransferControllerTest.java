package org.javaacademy.onlinebank;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.javaacademy.onlinebank.config.BankProperties;
import org.javaacademy.onlinebank.dto.PaymentDto;
import org.javaacademy.onlinebank.dto.TransferDto;
import org.javaacademy.onlinebank.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.javaacademy.onlinebank.OperationControllerTest.ACCOUNT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RequiredArgsConstructor
public class TransferControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private BankProperties properties;
    private final String BASE_URL = "http://localhost:";
    private final String BASE_URL_POSTFIX = "/transfer";
    private final String RECEIVE = "/operations/receive";
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void makeTransferSuccess() {
        PaymentDto paymentDto = deposit();
        TransferDto transferDto = new TransferDto(paymentDto.getToken(), "000001",
                new BigDecimal("500"), "test descr");
        RestAssured
                .given()
                .body(transferDto)
                .contentType(ContentType.JSON)
                .log().all()
                .post(BASE_URL + BASE_URL_POSTFIX)
                .then()
                .log().all()
                .statusCode(202);
    }

    private String createAccount() {
        String pin = userService.registration("+71112223344", "Test");
        String token = userService.authentication("+71112223344", pin);
        RestAssured
                .given()
                .queryParam("token", token)
                .log().all()
                .post(getBaseUrl() + ACCOUNT)
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
        return BASE_URL + properties.getPort();
    }
}
