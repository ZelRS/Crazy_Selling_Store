package crazy_selling_store.repository;


import crazy_selling_store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Интерфейс расширяющий JpaRepository для реализации запросов к БД к таблице пользователей
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    /**
    * Поиск пользователя по email(username)
    * @param email уникальный логин пользователя
    *
    * @return Optional контейнер с сущностью пользователя
     */
    Optional<UserEntity> findUserByEmail(String email);
}
