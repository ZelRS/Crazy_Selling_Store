package crazy_selling_store.controller;

import crazy_selling_store.dto.security.Login;
import crazy_selling_store.dto.security.Register;
import crazy_selling_store.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Tag(name = "Авторизация")
    public ResponseEntity<?> login(@RequestBody Login login) {
        log.info("Запрос на вход (Пользователь: " + login.getUsername() + ")");
        if (authService.login(login.getUsername(), login.getPassword())) {
            log.info("Вход выполнен успешно (Пользователь: " + login.getUsername() + ")");
            return ResponseEntity.ok().build();
        } else {
            log.info("Отказано во входе (Пользователь: " + login.getUsername() + ")");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    @Tag(name = "Регистрация")
    public ResponseEntity<?> register(@RequestBody Register register) {
        log.info("Запрос на регистрацию (Пользователь: " + register.getUsername() + ")");
        if (authService.register(register)) {
            log.info("Регистрация выполнена успешно (Пользователь: " + register.getUsername() + ")");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.info("Отказано в регистрации (Пользователь: " + register.getUsername() + ")");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
