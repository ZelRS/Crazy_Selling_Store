package crazy_selling_store.service;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

/**
 * Интерфейс сервиса для обработки комментариев.
 */
public interface CommentService {

    /**
     * Получает комментарии к объявлению по его ID.
     *
     * @param id     ID объявления
     * @return объект Comments, содержащий комментарии к объявлению
     */
    Comments getAdComments(Integer id);

    /**
     * Создает комментарий к объявлению.
     *
     * @param id             ID объявления
     * @param text           объект CreateOrUpdateComment с текстом комментария
     * @param authentication объект Authentication для аутентификации
     * @return созданный комментарий
     */
    Comment createAdComment(Integer id, CreateOrUpdateComment text, Authentication authentication);

    /**
     * Удаляет комментарий к объявлению.
     *
     * @param adId       ID объявления
     * @param commentId  ID комментария
     * @return true, если комментарий успешно удален, иначе false
     */
    boolean deleteAdComment(Integer adId, Integer commentId);

    /**
     * Обновляет текст комментария к объявлению.
     *
     * @param adId       ID объявления
     * @param commentId  ID комментария
     * @param text       объект CreateOrUpdateComment с обновленным текстом
     * @return обновленный комментарий
     */
    Comment updateAdComment(Integer adId, Integer commentId, CreateOrUpdateComment text);
}
