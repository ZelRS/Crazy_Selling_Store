package crazy_selling_store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурационный класс для создания бинов с настройкой аутентификации и хешированием паролей.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    /**
     * "Белый список" - эндпоинты, для которых не проверяются права доступа.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/ads",
            "/src/**"
    };

    /**
     * Бин настройки фильтра аутентификации.
     *
     * @param http HttpSecurity для конфигурации фильтра
     * @return SecurityFilterChain для конфигурации аутентификации
     * @throws Exception если произошла ошибка при создании фильтра
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        // Для "белого списка" полностью разрешен доступ без проверки прав.
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        // Для эндпоинтов ниже требуется аутентификация.
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Бин предназначенный для хэширования паролей в целях безопасности.
     *
     * @return PasswordEncoder для хэширования паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}