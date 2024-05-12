package org.javaacademy.onlinebank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.config.BankProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank-info")
@RequiredArgsConstructor
@Tag(name = "Контроллер банка")
public class BankController {
    private final BankProperties properties;

    @GetMapping
    @Operation(summary = "Вывод информации о банке")
    public ResponseEntity<String> getInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(properties.getName());

    }
}
