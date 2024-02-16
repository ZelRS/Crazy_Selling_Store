package crazy_selling_store.service;

import crazy_selling_store.dto.security.Register;

/**
 * Интерфейс сервиса для обработки авторизации и входа пользователя.
 */
public interface AuthService {

    /**
     * Аутентифицирует пользователя по имени пользователя и паролю.
     *
     * @param userName     имя пользователя для входа
     * @param password     пароль пользователя
     * @return true, если вход выполнен успешно, иначе false
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя по данным регистрации.
     *
     * @param register     объект с данными для регистрации нового пользователя
     * @return true, если регистрация прошла успешно, иначе false
     */
    boolean register(Register register);
}
