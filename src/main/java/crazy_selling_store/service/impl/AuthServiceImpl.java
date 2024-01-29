package crazy_selling_store.service.impl;

import crazy_selling_store.dto.security.Register;
import crazy_selling_store.mapper.UserMapper;
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
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(register.getRole().name())
                        .build());

//        /////////////////////добавил маппинг полей регистрации в сущность User и сохранил ее в БД//////////////////
//        crazy_selling_store.entity.User mappedUser = UserMapper.INSTANCE.registerToUser(register);
//        userRepository.save(mappedUser);
//        ////////////////////////////////////////////////////////////////////////////////////////////
        return true;
    }

}
