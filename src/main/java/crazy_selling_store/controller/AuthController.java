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

import static crazy_selling_store.mapper.UserMapper.INSTANCE;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Tag(name = "Авторизация")
    public ResponseEntity<?> login(@RequestBody(required = false) Login login) {
        log.info("поступил запрос на вход от пользователя " + login.getUsername());
        if (authService.login(INSTANCE.toEntityUser(login).getEmail(), INSTANCE.toEntityUser(login).getPassword())) {
            log.info("пользователь " + login.getUsername() + " вошел успешно");
            return ResponseEntity.ok().build();
        } else {
            log.info("Ошибка входа");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    @Tag(name = "Регистрация")
    public ResponseEntity<?> register(@RequestBody(required = false) Register register) {
        log.info("поступил запрос на регистрацию");
        if (authService.register(INSTANCE.toEntityUser(register))) {
            log.info("регистрация прошла успешно");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.info("регистрация провалена");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
