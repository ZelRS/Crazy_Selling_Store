package crazy_selling_store.service.impl;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.entity.UserEntity;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static crazy_selling_store.mapper.UserMapper.INSTANCE;

/**
 * Сервисный класс для обработки пользователей.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UploadImageService uploadImageService;

    /**
     * Устанавливает новый пароль пользователю.
     *
     * @param newPassword     объект с новым паролем
     * @param authentication  объект аутентификации пользователя
     * @return true, если пароль успешно установлен, иначе false
     */
    public boolean setPassword(NewPassword newPassword, Authentication authentication) {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            // Если такого пользователя нет, ловим исключение и возвращаем из метода false
            log.info("Пользователь не зарегистрирован");
            return false;
        }

        userEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
        repository.save(userEntity);
        return true;
    }

    /**
     * Получает информацию о пользователе.
     *
     * @param authentication  объект аутентификации пользователя
     * @return DTO пользователя или null, если пользователь не найден
     */
    public User getUserInformation(Authentication authentication) {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return null;
        }

        return INSTANCE.toDTOUser(userEntity);
    }

    /**
     * Обновляет информацию о пользователе.
     *
     * @param updateUser       объект с обновленными данными пользователя
     * @param authentication   объект аутентификации пользователя
     * @return объект с обновленной информацией о пользователе или null, если пользователь не найден
     */
    public UpdateUser updateUserInfo(UpdateUser updateUser, Authentication authentication) {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return null;
        }

        userEntity.setFirstName(updateUser.getFirstName());
        userEntity.setLastName(updateUser.getLastName());
        userEntity.setPhone(updateUser.getPhone());
        repository.save(userEntity);
        return updateUser;
    }

    /**
     * Обновляет аватар пользователя.
     *
     * @param image            объект с изображением аватара
     * @param authentication   объект аутентификации пользователя
     * @return true, если аватар успешно обновлен, иначе false
     * @throws IOException      если произошла ошибка ввода-вывода
     */
    public boolean updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return false;
        }

        String imageDir = "src/main/resources/userAvatars/";
        String origFilename = image.getOriginalFilename();

        assert origFilename != null;

        String savedFileName = userEntity.getEmail() + "." +
                               Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1));

        Path filePath = Path.of(imageDir, savedFileName);
        Files.createDirectories(filePath.getParent());
        uploadImageService.uploadImage(image, filePath);

        userEntity.setImage("/" + imageDir + savedFileName);
        repository.save(userEntity);
        return true;
    }
}
