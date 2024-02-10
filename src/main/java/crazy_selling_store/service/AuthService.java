package crazy_selling_store.service;

import crazy_selling_store.dto.security.Register;

//интерфейс сервиса для обработки авторизации и входа пользователя
public interface AuthService {
    boolean login(String userName, String password);
    boolean register(Register register);
}
