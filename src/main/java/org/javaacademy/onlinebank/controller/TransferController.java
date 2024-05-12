package org.javaacademy.onlinebank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.TransferDto;
import org.javaacademy.onlinebank.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
@Tag(name = "Блок межбанковских Переводов")
public class TransferController {
    private final BankService bankService;

    @PostMapping
    @Operation(summary = "Осуществление перевода")
    public ResponseEntity<?> makeTransfer(@RequestBody TransferDto transferDto) {
        bankService.transferToAnotherBank(
                transferDto.getToken(),
                transferDto.getAmount(),
                transferDto.getAccountNumber(),
                transferDto.getDescription());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}