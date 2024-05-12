package org.javaacademy.onlinebank.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.OperationDto;
import org.javaacademy.onlinebank.dto.PaymentDto;
import org.javaacademy.onlinebank.entity.Operation;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.service.BankService;
import org.javaacademy.onlinebank.service.OperationService;
import org.javaacademy.onlinebank.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operations")
@RequiredArgsConstructor
@Tag(name = "Блок Операций с деньгами",
        description = "Совершение платежа, внесение на счет")
public class OperationController {
    private final OperationService operationService;
    private final UserService userService;
    private final BankService bankService;

    @GetMapping()
    @io.swagger.v3.oas.annotations.Operation(summary = "Вывод операций пользователя")
    @ApiResponse(responseCode = "302",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Operation.class)))
    public ResponseEntity<Set<OperationDto>> findUserOperations(@RequestParam
                                                             @Parameter(description =
                                                                     "Вывод операций пользователя")
                                                             String token) {
        User user = userService.findByToken(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(operationService.findUserOperations(user));
    }

    @GetMapping("/account")
    public ResponseEntity<Set<OperationDto>> findOperationsByAccountNumber(@RequestParam
                                                           @Parameter(description
                                                                   = "Вывод операций "
                                                                   + "по номеру счета")
                                                           String accountNumber) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(operationService.findAllOperationsByAccount(accountNumber));
    }

    @PostMapping("/pay")
    @io.swagger.v3.oas.annotations.Operation(summary = "Совершение платежа")
    public ResponseEntity<?> makePayment(@RequestBody PaymentDto paymentDto) {
        bankService.makePayment(paymentDto);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/receive")
    @io.swagger.v3.oas.annotations.Operation(summary = "Внесение денег")
    public ResponseEntity<?> depositMoney(@RequestBody PaymentDto paymentDto) {
        bankService.depositMoney(paymentDto);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
