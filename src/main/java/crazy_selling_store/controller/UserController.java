package crazy_selling_store.controller;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content())})
    @PostMapping("/set_password")
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

    @Operation(
            summary = "Получение информации об авторизированном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = User.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content())})
    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserInformation(Authentication authentication) {
        User userInfo = userService.getUserInformation(authentication);
        if (userInfo == null) {
            log.info("Ошибка получения информации о пользователе (Пользователь: " + authentication.getName() + ")");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Информация о пользователе успешно получена (Пользователь: " + authentication.getName() + ")");
        return ResponseEntity.ok(userInfo);
    }

    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UpdateUser.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content())})
    @PatchMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
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

    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content())})
    @PatchMapping(value = "/me/image", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUserAvatar(@RequestParam("image") MultipartFile image,
                                                   Authentication authentication) throws IOException {
        log.info("Попытка смены аватара (Пользователь:  " + authentication.getName() + ")");
        if (!userService.updateUserAvatar(image, authentication)) {
            log.info("Пользователь не авторизован");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Аватар успешно обновлен (Пользователь:  " + authentication.getName() + ")");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
