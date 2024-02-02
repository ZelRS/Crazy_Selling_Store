package crazy_selling_store.service.impl;

import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    @Override
    public boolean login(String userName, String password) {
        Optional<crazy_selling_store.entity.User> user = userRepository.findUserByEmail(userName);
        if (user.isEmpty()) {
            log.info("Пользователь " + userName + " отсутствует");
            return false;
        } else if (!user.get().getPassword().equals(password)) {
            log.info("Пароль введен неверно");
            return false;
        }
//            UserDetails userDetails = manager.loadUserByUsername(userName);
        return true;
    }

    @Override
    public boolean register(crazy_selling_store.entity.User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            log.info("Такой пользователь существует");
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(user.getPassword())
                        .username(user.getEmail())
                        .roles(user.getRole().name())
                        .build());
        userRepository.save(user);
        return true;
    }

}