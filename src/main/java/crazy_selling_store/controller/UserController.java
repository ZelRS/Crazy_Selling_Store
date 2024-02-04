package crazy_selling_store.controller;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<String> setPassword(@RequestBody NewPassword newPassword,
                                              Authentication authentication) {
        log.info("Попытка смены пароля (Пользователь: " + authentication.getName() + ")");
        if (!authentication.isAuthenticated()) {
            log.info("Запрещено");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!userService.setPassword(newPassword, authentication)) {
            log.info("Ошибка смены пароля (Пользователь: " + authentication.getName() + ")");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Пароль успешно изменен (Пользователь: " + authentication.getName() + ")");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public ResponseEntity<User> getUserInformation(Authentication authentication) {
        User userInfo = userService.getUserInformation(authentication);
        if (userInfo == null) {
            log.info("Ошибка получения информации о пользователе (Пользователь: " + authentication.getName() + ")");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Информация о пользователе успешно получена (Пользователь: " + authentication.getName() + ")");
        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public ResponseEntity<UpdateUser> updateUserInfo(@RequestBody UpdateUser updateUser,
                                                     Authentication authentication) {
        UpdateUser updatedUserInfo = userService.updateUserInfo(updateUser, authentication);
        if (updatedUserInfo == null) {
            log.info("Ошибка обновления информации о пользователе (Пользователь: " + authentication.getName() + ")");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Информация о пользователе успешно обновлена (Пользователь: " + authentication.getName() + ")");
        return ResponseEntity.ok(updatedUserInfo);
    }


    @PatchMapping(value = "/me/image", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public ResponseEntity<String> updateUserAvatar(@RequestParam("image") MultipartFile image,
                                               Authentication authentication) throws IOException {
        log.info("vvvvvvvvvv");
        userService.updateUserAvatar(image, authentication);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
