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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder encoder;
    private final SpecialUserDetailsService manager;
    private final UserRepository repository;

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
            log.info("Введен не верный пароль (Пользователь: " + userName + ")");
            return false;
        }
        return true;
    }

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