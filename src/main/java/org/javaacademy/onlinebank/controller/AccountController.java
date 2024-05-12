package org.javaacademy.onlinebank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.AccountDto;
import org.javaacademy.onlinebank.entity.Account;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.service.AccountService;
import org.javaacademy.onlinebank.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Действия со счетом",
        description = "Методы по работе со счетами")
public class AccountController {
    private final UserService userService;
    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "Вывод всех счетов пользователя")
    @ApiResponse(responseCode = "202",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    public ResponseEntity<List<AccountDto>> findUserAccounts(@RequestParam
                                                          @Parameter(description = "Токен")
                                                          String token) {
        User user = findUserByToken(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(accountService.findUserAccounts(user));
    }

    @GetMapping("/{number}")
    @Operation(summary = "Вывод баланса")
    @ApiResponse(responseCode = "202",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BigDecimal.class)))
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String number,
                                                 @RequestParam
                                                 @Parameter(description = "Токен")
                                                 String token) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.showBalance(number, token));
    }

    @PostMapping
    @Operation(summary = "Создание счета")
    public ResponseEntity<String> createAccount(@RequestParam
                                                @Parameter(description = "Токен")
                                                String token) {
        User user = findUserByToken(token);
        Account account = accountService.createAccount(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(account.getAccountNumber());
    }


    private User findUserByToken(String token) {
        return userService.findByToken(token);
    }
}
