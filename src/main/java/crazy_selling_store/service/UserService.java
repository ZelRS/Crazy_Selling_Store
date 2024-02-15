package crazy_selling_store.service;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Интерфейс сервиса для обработки пользователей.
 */
public interface UserService {

    /**
     * Устанавливает новый пароль пользователю.
     *
     * @param newPassword     объект NewPassword с новым паролем
     * @param authentication  объект Authentication для аутентификации
     * @return true, если пароль успешно установлен, иначе false
     */
    boolean setPassword(NewPassword newPassword, Authentication authentication);

    /**
     * Получает информацию о текущем пользователе.
     *
     * @param authentication  объект Authentication для аутентификации
     * @return объект User с информацией о текущем пользователе
     */
    User getUserInformation(Authentication authentication);

    /**
     * Обновляет информацию о пользователе.
     *
     * @param updateUser      объект UpdateUser с обновленной информацией
     * @param authentication  объект Authentication для аутентификации
     * @return объект UpdateUser с обновленными данными
     */
    UpdateUser updateUserInfo(UpdateUser updateUser, Authentication authentication);

    /**
     * Обновляет аватар пользователя.
     *
     * @param image           объект MultipartFile с изображением аватара
     * @param authentication  объект Authentication для аутентификации
     * @return true, если аватар успешно обновлен, иначе false
     * @throws IOException если произошла ошибка ввода-вывода
     */
    boolean updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException;
}