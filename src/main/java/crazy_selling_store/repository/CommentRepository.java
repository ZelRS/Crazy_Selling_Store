package crazy_selling_store.repository;

import crazy_selling_store.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Интерфейс расширяющий JpaRepository для реализации запросов к БД к таблице комментариев
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Поиск комментария по идентификатору
     * @param id идентификатор комментария
     *
     * @return сущность комментария
     */
    CommentEntity getCommentByPk(Integer id);

    /**
     * Поиск комментария по идентификатору
     * @param pk идентификатор комментария
     *
     * @return Optional контейнер с сущностью комментария
     */
    Optional<CommentEntity> findByPk(Integer pk);
}
