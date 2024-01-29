package crazy_selling_store.controller;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {
    private final UserMapper userMapper;

    @PostMapping("/set-password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Void> updatePassword(@RequestBody(required = false) NewPassword newPassword) {
        int stub = 0; /*заглушка*/
        if (stub > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else if (stub == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public ResponseEntity<User> getUser() {
        User stubObj = new User(); /*объект заглушка*/
        return ResponseEntity.ok(stubObj);
    }

    @PatchMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public ResponseEntity<UpdateUser> updateInfo(@RequestBody(required = false) UpdateUser updateUser) {
        return ResponseEntity.ok(updateUser);
    }

    @PatchMapping(value = "/me/image", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public ResponseEntity<Void> updateAvatar(@RequestParam(name = "image") MultipartFile multipartFile) {
        int stub = 10; /*заглушка*/
        if (stub >= 10) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



}
