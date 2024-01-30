package crazy_selling_store.service.impl;

import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(crazy_selling_store.entity.User user) {
        if (manager.userExists(user.getEmail())) {
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