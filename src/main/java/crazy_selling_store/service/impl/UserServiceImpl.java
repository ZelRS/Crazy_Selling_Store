package crazy_selling_store.service.impl;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.entity.User;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public boolean setPassword(NewPassword newPassword, Authentication authentication) {
        User user;
        try {
            user = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return false;
        }
        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        repository.save(user);
        return true;
    }

}
