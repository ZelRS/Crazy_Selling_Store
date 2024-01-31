package crazy_selling_store.controller;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.mapper.UserMapper;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.impl.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @PostMapping("/set-password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Void> updatePassword(@RequestBody(required = false) NewPassword newPassword) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication != null ? authentication.getName() : null;
            if (loggedInUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            crazy_selling_store.entity.User user = userRepository.findUserByEmail(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            if (newPassword == null || StringUtils.isEmpty(newPassword.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public ResponseEntity<User> getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication != null ? authentication.getName() : null;

            if (loggedInUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            crazy_selling_store.entity.User user = userRepository.findUserByEmail(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            User userDTO = userMapper.toDTOUser(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public ResponseEntity<UpdateUser> updateInfo(@RequestBody(required = false) UpdateUser updateUser) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication != null ? authentication.getName() : null;

            if (loggedInUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            crazy_selling_store.entity.User user = userRepository.findUserByEmail(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            user = userMapper.toEntityUser(updateUser);
            user = userRepository.save(user);
            UpdateUser updatedUserDTO = userMapper.toDTOUpdateUser(user);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping(value = "/me/image", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public ResponseEntity<String> updateAvatar(@RequestParam("image") MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            String fileUrl = fileStorageService.saveFile(multipartFile);
            return ResponseEntity.ok(fileUrl);
        }
        return ResponseEntity.badRequest().build();
    }



}
