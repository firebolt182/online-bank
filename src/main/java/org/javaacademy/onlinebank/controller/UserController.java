package org.javaacademy.onlinebank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.dto.AuthDtoRq;
import org.javaacademy.onlinebank.dto.UserDto;
import org.javaacademy.onlinebank.entity.User;
import org.javaacademy.onlinebank.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Действия с пользователем",
        description = "Методы по созданию пользователей и их аутентификация")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "Создание пользователя")
    public ResponseEntity<String> signUp(@RequestBody UserDto body) {
        return ResponseEntity.status(HttpStatus.CREATED)
               .body(userService.registration(body.getPhoneNumber(), body.getName()).toString());
    }

    @PostMapping("/auth")
    @Operation(summary = "Аутентификация")
    public ResponseEntity<String> authentication(@RequestBody AuthDtoRq authDtoRq) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.authentication(authDtoRq.getPhoneNumber(),
                        authDtoRq.getPinCode()));
    }

    @GetMapping
    @Operation(summary = "Вывод всех пользователей")
    @ApiResponse(responseCode = "202",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)))
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.findAll());
    }
}
