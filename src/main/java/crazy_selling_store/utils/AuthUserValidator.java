package crazy_selling_store.utils;

import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.CommentEntity;
import crazy_selling_store.exceptions.EntityNotFoundException;
import crazy_selling_store.repository.AdRepository;
import crazy_selling_store.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Класс предназначен для использования внутри аннотации PreAuthorize для валидации залогиненного пользователя.
 */
@Service
@RequiredArgsConstructor
public class AuthUserValidator {
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;

    /**
     * Проверяет, является ли пользователь автором объявления.
     *
     * @param username  имя пользователя для проверки
     * @param id        идентификатор объявления
     * @return true, если пользователь является автором объявления, иначе false
     */
    public boolean isAdAuthor(String username, Integer id) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
        return ad.getUser().getEmail().equals(username);
    }

    /**
     * Проверяет, является ли пользователь автором комментария.
     *
     * @param username  имя пользователя для проверки
     * @param id        идентификатор комментария
     * @return true, если пользователь является автором комментария, иначе false
     */
    public boolean isCommentAuthor(String username, Integer id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден"));
        return comment.getUser().getEmail().equals(username);
    }
}
