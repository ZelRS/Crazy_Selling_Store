package crazy_selling_store.repository;

import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
* Интерфейс расширяющий JpaRepository для реализации запросов к БД к таблице объявлений
 */
public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    /**
     * Поиск всех объявлений конкретного пользователя
     * @param user пользователь
     *
     * @return список объявлений
     */
    List<AdEntity> findByUser(UserEntity user);

    /**
     * Поиск объявления по идентификатору
     * @param id идентификатор объявления
     *
     * @return сущность объявления
     */
    AdEntity getAdByPk(Integer id);

    /**
     * Поиск объявления по идентификатору
     * @param pk идентификатор пользователя
     *
     * @return Optional контейнер с сущностью объявления
     */
    Optional<AdEntity> findByPk(Integer pk);

    /**
     * Удаление пользователя по идентификатору
     * @param id идентификатор объявления
     */
    void deleteByPk(Integer id);
}
