package crazy_selling_store.service.impl;

import crazy_selling_store.dto.security.Register;
import crazy_selling_store.entity.UserEntity;
import crazy_selling_store.mapper.UserMapper;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервисный класс для обработки авторизации и входа пользователя.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final SpecialUserDetailsService manager;
    private final UserRepository repository;

    /**
     * Проверяет вход пользователя.
     *
     * @param userName имя пользователя
     * @param password пароль пользователя
     * @return true, если вход успешный, false в противном случае
     */
    @Override
    public boolean login(String userName, String password) {
        UserDetails userDetails;
        try {
            userDetails = manager.loadUserByUsername(userName);
        } catch (UsernameNotFoundException e) {
            log.info("Не зарегистрирован (Пользователь: " + userName + ")");
            return false;
        }
        if (!encoder.matches(password, userDetails.getPassword())) {
            log.info("Введен неверный пароль (Пользователь: " + userName + ")");
            return false;
        }
        return true;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param register данные нового пользователя
     * @return true, если регистрация успешная, false если пользователь уже зарегистрирован
     */
    @Override
    public boolean register(Register register) {
        try {
            manager.loadUserByUsername(register.getUsername());
        } catch (UsernameNotFoundException e) {
            User.builder()
                    .passwordEncoder(this.encoder::encode)
                    .password(register.getPassword())
                    .username(register.getUsername())
                    .roles(register.getRole().name())
                    .build();
            UserEntity user = UserMapper.INSTANCE.toEntityUser(register);
            user.setPassword(encoder.encode(register.getPassword()));
            repository.save(user);
            return true;
        }
        log.info("Уже зарегистрирован (Пользователь: " + register.getUsername() + ")");
        return false;
    }
}