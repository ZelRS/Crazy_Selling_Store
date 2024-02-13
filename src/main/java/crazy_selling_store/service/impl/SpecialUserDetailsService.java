package crazy_selling_store.service.impl;

import crazy_selling_store.entity.UserEntity;
import crazy_selling_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//сервисный класс для загрузки актуального пользователя в контекст Spring Secutity
@Service
@RequiredArgsConstructor
public class SpecialUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //находим пользователя в БД по email
        UserEntity user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        //загружаем найденного пользователя в контекст Spring Security и возвращаем его
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
